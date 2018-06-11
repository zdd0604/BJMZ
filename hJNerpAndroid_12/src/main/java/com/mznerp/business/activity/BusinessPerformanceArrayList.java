package com.mznerp.business.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mznerp.activity.MainActivity;
import com.mznerp.business.adapter.BusinessBillsAdapter;
import com.mznerp.business.BusinessJsonCallBack.BJsonCallBack;
import com.mznerp.business.businessutils.DateUtil;
import com.mznerp.common.ActivitySupport;
import com.mznerp.common.Constant;
import com.mznerp.common.EapApplication;
import com.mznerp.model.BusinessBillsMessages;
import com.mznerp.model.PerformanceDatas;
import com.mznerp.util.StringUtil;
import com.mznerp.util.ToastUtil;
import com.mznerp.widget.WaitDialogRectangle;
import com.mznerp.R;
import com.lzy.okgo.OkGo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 计划录入列表界面
 */
public class BusinessPerformanceArrayList extends ActivitySupport implements View.OnClickListener {
    private WaitDialogRectangle waitDialog;
    private PullToRefreshListView pull_refresh_billlist;
    private Button addperformanc_array;
    private List<PerformanceDatas> datas = new ArrayList<>();
    private final int HTTP_SUCCESS = 0;//数据请求成功
    private final int HTTP_LOSER = 1;//数据请求成功
    private BusinessBillsAdapter billsAdapter;
    private int index;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    setViewData();
                    break;
                case 1:
                    String content = (String) msg.obj;
                    ToastUtil.ShowLong(BusinessPerformanceArrayList.this, content);
                    waitDialog.dismiss();
                    pull_refresh_billlist.onRefreshComplete();
                    break;
                case 2:
                    datas.remove(index);
                    refreshList();
                    break;
            }
        }
    };

    /**
     * 刷新listview中的数据
     */
    private void refreshList() {
        billsAdapter.refreshList(datas);
        // listItemAdapter.notifyDataSetChanged();
        pull_refresh_billlist.onRefreshComplete();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActionBar = getSupportActionBar();
        setContentView(R.layout.activity_business_performance_array_list);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setTitle("绩效计划录入");
        initView();
        waitDialog = new WaitDialogRectangle(this);
        waitDialog.show();
    }

    private void initView() {
        pull_refresh_billlist = (PullToRefreshListView) findViewById(R.id.pull_refresh_billlist);
        // pull_refresh_bills.setMode(Mode.BOTH);//上下拉刷新
        pull_refresh_billlist.setMode(PullToRefreshBase.Mode.PULL_FROM_START);// 仅下拉刷新

        addperformanc_array = (Button) findViewById(R.id.addperformanc_array);
        addperformanc_array.setOnClickListener(this);

        addListViewData();

        // //上下拉刷新
        pull_refresh_billlist.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getBusinessList(Constant.ID_MENU, Constant.ej1345.getId_user());
            }


            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
            }

        });
    }

    private void setViewData() {
        Collections.sort(datas, new Comparator<PerformanceDatas>() {
            @Override
            public int compare(PerformanceDatas lhs, PerformanceDatas rhs) {
                Date date1 = DateUtil.stringToDate(lhs.getMain().getDate_opr());
                Date date2 = DateUtil.stringToDate(rhs.getMain().getDate_opr());
                // 对日期字段进行升序，如果欲降序可采用after方法
                if (date1.before(date2)) {
                    return 1;
                }
                return -1;

            }
        });
        for (PerformanceDatas pd : datas) {
            if (pd.getMain() != null && pd.getDetails() != null) {
                billsAdapter = new BusinessBillsAdapter(this, datas);
                pull_refresh_billlist.setAdapter(billsAdapter);
                billsAdapter.notifyDataSetChanged();
                pull_refresh_billlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Constant.performanceDatas = getPerformanceDatas(position - 1);
                        Constant.JUDGE_TYPE = false;
                        Intent intent = new Intent(BusinessPerformanceArrayList.this, BusinessPerformanceInput.class);
                        startActivityForResult(intent, 11);
                    }
                });
                waitDialog.dismiss();
                pull_refresh_billlist.onRefreshComplete();
            } else {
                sendMessage(HTTP_LOSER, "数据为空");
            }
        }
    }

    // 获得WorkInfo
    private PerformanceDatas getPerformanceDatas(int position) {
        PerformanceDatas info = null;
        index = position;
        info = datas.get(position);
        return info;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // //界面返回值
        /**
         * @author haijian
         * 收到返回的值判断是否成功，如果同意就将数据移除刷新列表
         */
        if (requestCode == 11 && resultCode == 22) {
            handler.sendEmptyMessage(2);
            MainActivity.WORK_COUNT = MainActivity.WORK_COUNT - 1;
        } else if (requestCode == 11 && resultCode == 33) {
            handler.sendEmptyMessage(2);
        }
    }

    /**
     * 从网络加载数据
     */
    private void addListViewData() {
        // 如果有网络加载网络数据，并在加载成功后删除之前保留的本地数据，没有网络加载本地数据
        if (this.hasInternetConnected()) {
            if (StringUtil.isStrTrue(Constant.ID_MENU)) {
                getBusinessList(Constant.ID_MENU, Constant.ej1345.getId_user());
            }
        } else {
            sendMessage(HTTP_LOSER, "请检查网络");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addperformanc_array:
                intentActivity(BusinessPerformanceTypeIn.class);
                Constant.JUDGE_TYPE = true;
                break;
        }
    }

    private void intentActivity(Class c) {
        Intent itent = new Intent(BusinessPerformanceArrayList.this, c);
        startActivity(itent);
    }

    /**
     * 获取数据
     *
     * @param idmenu
     * @param iduser
     */
    private void getBusinessList(String idmenu, String iduser) {
        OkGo.post(EapApplication.URL_SERVER_HOST_HTTP + "/servlet/DataQueryServlet")
                .params("idmenu", idmenu)
                .params("iduser", iduser)
                .execute(new BJsonCallBack<BusinessBillsMessages>() {
                    @Override
                    public void onSuccess(BusinessBillsMessages businessBillsMessages, Call call, Response response) {
                        datas = businessBillsMessages.getDatas();
                        handler.sendEmptyMessage(HTTP_SUCCESS);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        sendMessage(HTTP_LOSER, "数据获取失败");
                    }

                });
    }

    private void sendMessage(int tag, Object content) {
        Message message = new Message();
        message.what = tag;
        message.obj = content;
        handler.sendMessage(message);
    }

}
