package com.hjnerp.business.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hjnerp.activity.MainActivity;
import com.hjnerp.adapter.BusinessBillsAdapter;
import com.hjnerp.business.BusinessJsonCallBack.BJsonCallBack;
import com.hjnerp.business.businessutils.DateUtil;
import com.hjnerp.common.ActionBarWidgetActivity;
import com.hjnerp.common.Constant;
import com.hjnerp.common.EapApplication;
import com.hjnerp.dao.BusinessBaseDao;
import com.hjnerp.dao.QiXinBaseDao;
import com.hjnerp.model.BusinessBillsMessages;
import com.hjnerp.model.Ctlm1345;
import com.hjnerp.model.Ej1345;
import com.hjnerp.model.PerformanceDatas;
import com.hjnerp.util.StringUtil;
import com.hjnerp.util.ToastUtil;
import com.hjnerp.widget.WaitDialogRectangle;
import com.hjnerpandroid.R;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

public class TravelActivityNew extends ActionBarWidgetActivity implements View.OnClickListener {
    private WaitDialogRectangle waitDialog;
    private PullToRefreshListView refresh_travel_act;
    private Button add_travel_act;
    private RelativeLayout activity_travel_new;
    private List<PerformanceDatas> datas = new ArrayList<>();
    private final int HTTP_SUCCESS = 0;//数据请求成功
    private final int HTTP_LOSER = 1;//数据请求成功
    private BusinessBillsAdapter billsAdapter;
    private int index;
    private List<Ctlm1345> users;
    private String userID;
    private String id_menu = Constant.ID_MENU;
    public static TravelActivityNew travelActivityNew = null;
    private String id_recorder = QiXinBaseDao.queryCurrentUserInfo().userID;
    private String table_name = "";
    private String table_no = "";
    private String column_name = "";

//    @BindView(R.id.action_left_img)
//    ImageView actionLeftImg;
    @BindView(R.id.action_left_tv)
    TextView actionLeftTv;
//    @BindView(R.id.action_back_layout)
//    LinearLayout actionBackLayout;
    @BindView(R.id.action_center_tv)
    TextView actionCenterTv;
    @BindView(R.id.action_right_tv)
    TextView actionRightTv;

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
                    ToastUtil.ShowLong(getApplicationContext(), content);
                    waitDialog.dismiss();
                    refresh_travel_act.onRefreshComplete();
                    break;
                case 2:
                    datas.remove(index);
                    refreshList();
                    break;
            }
        }
    };

    public void refresh() {
        datas.remove(index);
//        refresh_travel_act.onRefreshComplete();
        refreshList();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_new);
        ButterKnife.bind(this);
        travelActivityNew = this;
        initView();
    }

    /**
     * 刷新listview中的数据
     */
    private void refreshList() {
        billsAdapter.refreshList(datas);
        // listItemAdapter.notifyDataSetChanged();
        refresh_travel_act.onRefreshComplete();
    }

    private void initView() {
        actionRightTv.setOnClickListener(this);
        actionRightTv.setText(getString(R.string.action_right_content_add));
        actionLeftTv.setOnClickListener(this);
        refresh_travel_act = (PullToRefreshListView) findViewById(R.id.refresh_travel_act);
        add_travel_act = (Button) findViewById(R.id.add_travel_act);
        activity_travel_new = (RelativeLayout) findViewById(R.id.activity_travel_new);
        switch (id_menu) {
            case "002035":
                getSupportActionBar().setTitle("出差/外出单");
                break;
            case "002040":
                getSupportActionBar().setTitle("休假申请单");
                break;
            case "002090":
                getSupportActionBar().setTitle("加班申请单");
                break;
            case "002095":
                getSupportActionBar().setTitle("考勤异常申诉");
                break;
            case "002020":
                actionCenterTv.setText(getString(R.string.action_center_content_sellList));
                break;
        }
        users = new ArrayList<>();
        users = BusinessBaseDao.getCTLM1345ByIdTable("user");
        if (users.size() == 0) {
            ToastUtil.ShowShort(this, "请先下载基础数据");
            finish();
            return;
        }
        String userinfos = users.get(0).getVar_value();
//        id_clerk =
        Gson gson1 = new Gson();
        Ej1345 ej1345 = gson1.fromJson(userinfos, Ej1345.class);
        userID = ej1345.getId_user();
        add_travel_act.setOnClickListener(this);
        waitDialog = new WaitDialogRectangle(this);
        waitDialog.show();
        refresh_travel_act.setMode(PullToRefreshBase.Mode.PULL_FROM_START);// 仅下拉刷新
        addListViewData();

        // //上下拉刷新
        refresh_travel_act.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getBusinessList(id_menu, userID);
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
            if (pd.getMain() != null) {
                billsAdapter = new BusinessBillsAdapter(this, datas);
                refresh_travel_act.setAdapter(billsAdapter);
                billsAdapter.notifyDataSetChanged();
                refresh_travel_act.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Constant.performanceDatas = getPerformanceDatas(position - 1);
                        Constant.JUDGE_TYPE = false;
                        Constant.JUDGE_TYPE1 = false;
                        Log.e("datas", Constant.performanceDatas.toString());
                        Intent intent = new Intent();

                        switch (id_menu) {
                            case "002035":
                                intent.setClass(getApplicationContext(), TravelBusiness.class);
                                break;
                            case "002040":
                                intent.setClass(getApplicationContext(), LeaveBusiness.class);

                                break;
                            case "002090":
                                intent.setClass(getApplicationContext(), OverBusiness.class);

                                break;
                            case "002095":
                                intent.setClass(getApplicationContext(), AbnormalBusiness.class);

                                break;
                            case "002020":
                                intent.setClass(getApplicationContext(), SellOrder.class);

                                break;
                            default:
                                break;

                        }
                        startActivityForResult(intent, 11);
                    }
                });
                refresh_travel_act.getRefreshableView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                        PerformanceDatas performanceDatas = getPerformanceDatas(i - 1);
                        Log.d("performanceDatas", performanceDatas.toString());

                        switch (id_menu) {
                            case "002035":
                                table_name = "dgtdout_03";
                                table_no = performanceDatas.getMain().getDgtdout_no();
                                column_name = "dgtdout_no";
                                break;
                            case "002040":
                                table_name = "dgtdvat_03";
                                table_no = performanceDatas.getMain().getDgtdvat_no();
                                column_name = "dgtdvat_no";
                                break;
                            case "002090":
                                table_name = "dgtdot_03";
                                table_no = performanceDatas.getMain().getDgtdout_no();
                                column_name = "dgtdot_no";
                                break;
                            case "002095":
                                table_name = "dgtdabn_03";
                                table_no = performanceDatas.getMain().getDgtdabn_no();
                                column_name = "dgtdabn_no";
                                break;
                            case "002020":
                                //dsaord
                                table_name = "dsaord_03";
                                table_no = performanceDatas.getMain().getDsaord_no();
                                column_name = "dsaord_no";
                                break;
                            default:
                                break;

                        }
                        DeleteDialog(mContext);
                        return true;
                    }
                });
                waitDialog.dismiss();
                refresh_travel_act.onRefreshComplete();
            } else {
                sendMessage(HTTP_LOSER, "数据为空");
            }
        }
    }

    private void DeleteDialog(Context context) {
//        mDrawerLayout.closeDrawer(Gravity.LEFT);
        final Dialog noticeDialog = new Dialog(context, R.style.noticeDialogStyle);
        noticeDialog.setContentView(R.layout.dialog_notice_withcancel);

        RelativeLayout dialog_cancel_rl, dialog_confirm_rl;
        TextView notice = (TextView) noticeDialog.findViewById(R.id.dialog_notice_tv);
        notice.setText("确认要删除单据：" + table_no + "吗?");
        dialog_cancel_rl = (RelativeLayout) noticeDialog
                .findViewById(R.id.dialog_cc_cancel_rl);
        dialog_confirm_rl = (RelativeLayout) noticeDialog
                .findViewById(R.id.dialog_cc_confirm_rl);
        dialog_cancel_rl.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                noticeDialog.dismiss();
            }
        });
        dialog_confirm_rl.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                noticeDialog.dismiss();
                deleteTable();
            }
        });

        noticeDialog.show();
    }

    private void deleteTable() {
        OkGo.post(EapApplication.URL_SERVER_HOST_HTTP + "/servlet/DeleteOrderEdit")
                .params("id_recorder", id_recorder)
                .params("table_name", table_name)
                .params("table_no", table_no)
                .params("column_name", column_name)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        if (s.contains("单据删除成功")) {
                            ToastUtil.ShowShort(getApplicationContext(), s);
                            handler.sendEmptyMessage(2);
                        } else {
                            ToastUtil.ShowShort(getApplicationContext(), "删除单据失败");
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        ToastUtil.ShowShort(getApplicationContext(), "删除单据失败");
                    }
                });
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
            if (StringUtil.isStrTrue(id_menu)) {
                getBusinessList(id_menu, userID);
            }
        } else {
            sendMessage(HTTP_LOSER, "请检查网络");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_right_tv:
                switch (id_menu) {
                    case "002035":
                        intentActivity(TravelBusiness.class);
                        break;
                    case "002040":
                        intentActivity(LeaveBusiness.class);
                        break;
                    case "002090":
                        intentActivity(OverBusiness.class);
                        break;
                    case "002095":
                        intentActivity(AbnormalBusiness.class);
                        break;
                    case "002020":
                        intentActivity(SellOrder.class);
                        break;
                    default:
                        break;
                }
                Constant.JUDGE_TYPE = true;
                break;
            case R.id.action_left_tv:
                finish();
                break;
        }
    }

    private void intentActivity(Class c) {
        Intent itent = new Intent(this, c);
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
                        Log.e("datas", datas.toString());
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
