package com.hjnerp.business.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hjnerp.adapter.BusinessBillsAdapter;
import com.hjnerp.business.BusinessJsonCallBack.BJsonCallBack;
import com.hjnerp.business.businessutils.DateUtil;
import com.hjnerp.common.ActivitySupport;
import com.hjnerp.common.Constant;
import com.hjnerp.common.EapApplication;
import com.hjnerp.model.BusinessBillsMessages;
import com.hjnerp.model.PerformanceDatas;
import com.hjnerp.util.StringUtil;
import com.hjnerp.util.ToastUtil;
import com.hjnerp.widget.WaitDialogRectangle;
import com.hjnerpandroid.R;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class BusinessBillsActivity extends ActivitySupport implements View.OnClickListener {
    protected WaitDialogRectangle waitDialogRectangle;
    private PullToRefreshListView pull_refresh_bills;
    private BusinessBillsAdapter billsAdapter;
    private int index;
    private List<PerformanceDatas> datas = new ArrayList<>();
    private final int HTTP_SUCCESS = 0;//数据请求成功
    private final int HTTP_LOSER = 1;//数据请求成功

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
                    ToastUtil.ShowLong(BusinessBillsActivity.this, content);
                    waitDialogRectangle.dismiss();
                    pull_refresh_bills.onRefreshComplete();
                    break;
                case 2:
                    datas.remove(index);
                    refreshList();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActionBar = getSupportActionBar();
        setContentView(R.layout.activity_business_bills);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        if (Constant.ID_MENU.equals("002055")) {
            mActionBar.setTitle("绩效计划审核");
        }
        if (Constant.ID_MENU.equals("002060")) {
            mActionBar.setTitle("绩效计划确认");
        }
        if (Constant.ID_MENU.equals("002070")) {
            mActionBar.setTitle("绩效完成情况自述");
        }
        if (Constant.ID_MENU.equals("002075")) {
            mActionBar.setTitle("绩效完成情况评价");
        }

        if (Constant.ID_MENU.equals("002080")) {
            mActionBar.setTitle("绩效评价结果确认");
        }
        waitDialogRectangle = new WaitDialogRectangle(this);
        waitDialogRectangle.show();
        initView();
    }

    private void initView() {
        pull_refresh_bills = (PullToRefreshListView) findViewById(R.id.pull_refresh_bills);
        // pull_refresh_bills.setMode(Mode.BOTH);//上下拉刷新
        pull_refresh_bills.setMode(PullToRefreshBase.Mode.PULL_FROM_START);// 仅下拉刷新

        addListViewData();

        // //上下拉刷新
        pull_refresh_bills.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getBusinessList(Constant.ID_MENU, Constant.ej1345.getId_user());
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            }

        });

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

        }
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
                pull_refresh_bills.setAdapter(billsAdapter);
                billsAdapter.notifyDataSetChanged();
                pull_refresh_bills.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Constant.performanceDatas = getPerformanceDatas(position - 1);
                        Intent intent = new Intent(BusinessBillsActivity.this, BusinessPerformanceAudit.class);
                        startActivityForResult(intent, 11);
                    }
                });
                waitDialogRectangle.dismiss();
                pull_refresh_bills.onRefreshComplete();
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
        } else if (requestCode == 11 && resultCode == 33) {
            handler.sendEmptyMessage(2);
        }
    }


    /**
     * 刷新listview中的数据
     */
    private void refreshList() {
        billsAdapter.refreshList(datas);
        // listItemAdapter.notifyDataSetChanged();
        pull_refresh_bills.onRefreshComplete();
    }

    private void sendMessage(int tag, Object content) {
        Message message = new Message();
        message.what = tag;
        message.obj = content;
        handler.sendMessage(message);
    }

}
