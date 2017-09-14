package com.hjnerp.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hjnerp.business.Ctlm1345Update;
import com.hjnerp.business.Ctlm1346Update;
import com.hjnerp.business.Ctlm1347UpdateAgain;
import com.hjnerp.common.ActionBarWidgetActivity;
import com.hjnerp.dao.BaseDao;
import com.hjnerp.model.CommonSetInfo;
import com.hjnerp.util.Command;
import com.hjnerp.util.Command.OnResultListener;
import com.hjnerp.util.ToastUtil;
import com.hjnerp.util.VersionManager;
import com.hjnerpandroid.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SetActivity extends ActionBarWidgetActivity implements OnClickListener {

    protected static final String TAG = "SetActivity";
    //    private TextView tv_tologin_without_clean, tv_tologin_with_clean;
    private ArrayList<CommonSetInfo> commonSetInfoList = new ArrayList<CommonSetInfo>();
    private Thread mThread;
    private RelativeLayout dialog_cancel_rl, dialog_confirm_rl;
    private Dialog noticeDialog;
    private static String DOWNLOAD_XML_SUCCESS = "download_xml_success";
    private static String DOWNLOAD_XML_CONTAINS_ERROR = "download_xml_contains_error";

    private static String DOWNLOAD_CTLM1347_SUCCESS = "download_1347_success";
    private static String DOWNLOAD_CTLM1347_CONTAINS_ERROR = "download_1347_contains_error";

    private int withclean_counts, withoutclean_counts;
//    private Handler mHandler;
//    private boolean mRunning = true;
//    private int flag = 0;

    @BindView(R.id.action_left_tv)
    TextView actionLeftTv;
    @BindView(R.id.action_center_tv)
    TextView actionCenterTv;
    @BindView(R.id.action_right_tv)
    TextView actionRightTv;
    @BindView(R.id.action_right_tv1)
    TextView actionRightTv1;
    @BindView(R.id.rel_wrapdata)
    TextView rel_wrapdata;
    @BindView(R.id.rel_wrapcache)
    TextView rel_wrapcache;
    @BindView(R.id.rel_versioncheck)
    TextView rel_versioncheck;
    @BindView(R.id.rel_updata)
    TextView rel_updata;
    @BindView(R.id.rel_updatadate)
    TextView rel_updatadate;
    @BindView(R.id.rel_updatabasedate)
    TextView rel_updatabasedate;
    @BindView(R.id.rel_setpwd)
    TextView rel_setpwd;
    @BindView(R.id.rel_hjabout)
    TextView rel_hjabout;
    @BindView(R.id.app_logout)
    Button app_logout;
    //    @BindView(R.id.tv_with_clean)
//    TextView tv_tologin_with_clean;
//    @BindView(R.id.tv_without_clean)
//    TextView tv_tologin_without_clean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commonset);
        ButterKnife.bind(this);

        actionRightTv.setVisibility(View.GONE);
        actionLeftTv.setOnClickListener(this);
        actionCenterTv.setText(getString(R.string.setting_Title_TlActivity));
        app_logout.setOnClickListener(this);
        rel_hjabout.setOnClickListener(this);
        rel_wrapdata.setOnClickListener(this);
        rel_wrapcache.setOnClickListener(this);
        rel_versioncheck.setOnClickListener(this);
        rel_updata.setOnClickListener(this);
        rel_updatadate.setOnClickListener(this);
        rel_updatabasedate.setOnClickListener(this);
        rel_setpwd.setOnClickListener(this);
        waitDialog.setCanceledOnTouchOutside(false);

//        tv_tologin_with_clean.setOnClickListener(this);
//        tv_tologin_without_clean.setOnClickListener(this);

//		HandlerThread thread = new HandlerThread("HandlerThread");
//		thread.start();//创建一个HandlerThread并启动它
//		mHandler = new Handler(thread.getLooper());//使用HandlerThread的looper对象创建Handler，如果使用默认的构造方法，很有可能阻塞UI线程
//		mHandler.post(mBackgroundRunnable);//将线程post到Handler中

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.tv_with_clean:
//                withoutclean_counts = 0;
//                withclean_counts++;
//                if (withclean_counts == 5) {
//
////					newChatMessageList.clear();
//                    // 清除联系人新消息
////					cleanNewContactCount();
//                    EapApplication eap = getEapApplication();
////					eap.putExtra(EapApplication.EXTRA_SESSION_ID,"");
//                    sputil.setMySessionId("");
////					QiXinBaseDao.updateUserInfo((String) eap.getExtra(EapApplication.EXTRA_USER_ID),
////							Tables.UserTable.COL_VAR_SESSION,"");
////
//
//                    ToastUtil.ShowShort(SetActivity.this, "程序重启");
//                    exitAPP();
////					throw new NullPointerException();
//
//                }
//                break;
//            case R.id.tv_without_clean:
//                withclean_counts = 0;
//                withoutclean_counts++;
//                if (withoutclean_counts == 5) {
//                    ToastUtil.ShowShort(SetActivity.this, "程序重启");
////					throw new NullPointerException();
//
//                    exitAPP();
//                }
//                break;
            case R.id.rel_wrapdata:
                showNoticeDialog("确定清除数据吗？", 0);
                break;
            case R.id.rel_wrapcache:
                showNoticeDialog("确定清除缓存吗？", 1);
                break;
            case R.id.rel_versioncheck:
                showNoticeDialog("检查新版本", 2);
                break;
            case R.id.rel_updata:
                showNoticeDialog("是否更新应用？", 3);
                break;
            case R.id.rel_updatadate:
                showNoticeDialog("是否下载应用数据？", 4);
                break;
            case R.id.rel_updatabasedate:
                showNoticeDialog("是否下载基础数据？", 5);
                break;
            case R.id.rel_setpwd:
                Intent intentpas = new Intent(SetActivity.this, SetPassWordActivity.class);
                startActivity(intentpas);
                break;
            case R.id.rel_hjabout:
                Intent intent = new Intent(SetActivity.this, AboutHJActivity.class);
                startActivity(intent);
                break;
            case R.id.app_logout:
                LogOut(SetActivity.this);
//                onclickAppLogin.onclickOutLogin();
                break;
            case R.id.action_left_tv:
                finish();
                break;
        }
    }

    /**
     * 退出登录
     *
     * @param context
     */
    public void LogOut(Context context) {
        final Dialog noticeDialog = new Dialog(context, R.style.noticeDialogStyle);
        noticeDialog.setContentView(R.layout.dialog_notice_withcancel);

        RelativeLayout dialog_cancel_rl, dialog_confirm_rl;
        TextView notice = (TextView) noticeDialog.findViewById(R.id.dialog_notice_tv);
        notice.setText("确认要退出和佳ERP吗?");
        dialog_cancel_rl = (RelativeLayout) noticeDialog
                .findViewById(R.id.dialog_cc_cancel_rl);
        dialog_confirm_rl = (RelativeLayout) noticeDialog
                .findViewById(R.id.dialog_cc_confirm_rl);
        dialog_cancel_rl.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                noticeDialog.dismiss();
            }
        });
        dialog_confirm_rl.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                noticeDialog.dismiss();
                isExit();
            }
        });

        noticeDialog.show();
    }

    @Override
    public void isExit() {
        super.isExit();
        LogoutTask logoutTask = new LogoutTask(this);
        logoutTask.execute();
    }

    public void exitAPP() {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(SetActivity.this, ExitAppActivity.class);
        startActivity(intent);
    }

    private OnClickListener onClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (noticeDialog != null || noticeDialog.isShowing()) {
                noticeDialog.dismiss();
            }
            switch (v.getId()) {
                case R.id.dialog_cc_cancel_rl:

                    break;
                case R.id.dialog_cc_confirm_rl:
                    int tag = (Integer) dialog_confirm_rl.getTag();
                    if (tag == 0) {
                        //清除数据
                        BaseDao.wrapData();
                        ToastUtil.ShowShort(SetActivity.this, "清除成功！");
                    }
                    if (tag == 1) {
                        //清楚缓存
                        BaseDao.wrapcache();
                        ToastUtil.ShowShort(SetActivity.this, "清除成功！");
                    }
                    if (tag == 2) {
                        //检查版本
                        VersionManager vm = VersionManager.getSharedInstance();
                        vm.checkVersionUpgrade(false, SetActivity.this, new VersionManager.OnUpgradeResultListener() {
                            @Override
                            public void onUpgradeResult(boolean success, String msg) {
                                if (!success) {
                                    ToastUtil.ShowShort(SetActivity.this, msg);
                                }
                            }
                        });
                    }
                    if (tag == 3) {
                        //更新应用（更新xml模板）
                        if (hasInternetConnected()) {
                            updateModelThread();
                        } else {
                            ToastUtil.ShowShort(SetActivity.this, getResources().getString(R.string.check_connection));
                        }
                    }
                    if (tag == 4) {
                        //TODO 下载1347
                        if (hasInternetConnected()) {
                            updateCtlm1347Thread();
                        } else {
                            ToastUtil.ShowShort(SetActivity.this, getResources().getString(R.string.check_connection));
                        }
                    }
                    if (tag == 5) {//手动下载基础数据
                        if (hasInternetConnected()) {
                            updateCtlm1345Thread();
                        } else {
                            ToastUtil.ShowShort(SetActivity.this, getResources().getString(R.string.check_connection));
                        }
                    }
                    break;

                default:
                    break;
            }

        }
    };

    private void updateModelThread() {
        waitDialog.show();
        mThread = new Thread() {
            @Override
            public void run() {
                new Ctlm1346Update(new Command.OnMultiResultListener() {
                    @Override
                    public void onResult(List<Boolean> successes) {
                        if (successes.contains(false)) {
                            Log.e(TAG, "Ctlm1346Update contains false");
                            sendToHandler(DOWNLOAD_XML_CONTAINS_ERROR);
                        } else {
                            sendToHandler(DOWNLOAD_XML_SUCCESS);
                        }

                    }
                }).action();//下载xml
            }
        };
        mThread.start();
    }

    private void updateCtlm1347Thread() {

        waitDialog.show();

        mThread = new Thread() {
            @Override
            public void run() {
                new Ctlm1347UpdateAgain(new OnResultListener() {
                    @Override
                    public void onResult(boolean success) {
                        // TODO Auto-generated method stub
                        if (success) {
                            sendToHandler(DOWNLOAD_CTLM1347_SUCCESS);
                        } else {
                            Log.e(TAG, "Ctlm1347Update contains false");
                            sendToHandler(DOWNLOAD_CTLM1347_CONTAINS_ERROR);
                        }

                    }

                }).action();

            }
        };
        mThread.start();
    }

    /**
     * 更新基础数据
     */
    private void updateCtlm1345Thread() {
        waitDialog.show();
        waitDialog.setText("更新数据中");
        mThread = new Thread() {
            @Override
            public void run() {//new Ctlm1345Update(new String[]{"ctlm4101"}, new String[]{"1=1"}, null).action();
                new Ctlm1345Update(new String[]{"ctlm1345"}, new String[]{"2=2"}, new OnResultListener() {

                    @Override
                    public void onResult(boolean success) {
                        if (success) {

                            sendToHandler(DOWNLOAD_CTLM1347_SUCCESS);
                        } else {
                            sendToHandler(DOWNLOAD_CTLM1347_CONTAINS_ERROR);
                        }
                    }
                }).action();
            }
        };
        mThread.start();
    }

    private void sendToHandler(String msg) {
        Message Msg = new Message();
        Bundle b = new Bundle();
        b.putString("flag", msg);
        Msg.setData(b);

        myHandler.sendMessage(Msg);
    }

    final Handler myHandler = new Handler() {
        @SuppressLint("HandlerLeak")
        public void handleMessage(Message msg) {

            Bundle b = msg.getData();
            String mmsg = b.getString("flag");

            if (waitDialog != null && waitDialog.isShowing()) {
                waitDialog.dismiss();
            }

            if (DOWNLOAD_XML_CONTAINS_ERROR.equalsIgnoreCase(mmsg)) {
                ToastUtil.ShowShort(SetActivity.this, "更新失败，请重试！");
            } else if (DOWNLOAD_XML_SUCCESS.equalsIgnoreCase(mmsg)) {
                ToastUtil.ShowShort(SetActivity.this, "更新成功！");
                MainActivity.need_fresh_businessmenu = true;
            } else if (DOWNLOAD_CTLM1347_SUCCESS.equalsIgnoreCase(mmsg)) {
                ToastUtil.ShowShort(SetActivity.this, "下载成功！");
            } else if (DOWNLOAD_CTLM1347_CONTAINS_ERROR.equalsIgnoreCase(mmsg)) {
                ToastUtil.ShowShort(SetActivity.this, "下载失败，请重试！");
            }
        }

        ;
    };

    public void showNoticeDialog(String text, int tag) {

        noticeDialog = new Dialog(this, R.style.noticeDialogStyle);
        noticeDialog.setContentView(R.layout.dialog_notice_withcancel);

        TextView notice = (TextView) noticeDialog
                .findViewById(R.id.dialog_notice_tv);
        notice.setText(text);
        dialog_cancel_rl = (RelativeLayout) noticeDialog
                .findViewById(R.id.dialog_cc_cancel_rl);
        dialog_confirm_rl = (RelativeLayout) noticeDialog
                .findViewById(R.id.dialog_cc_confirm_rl);
        dialog_confirm_rl.setTag(tag);

        dialog_cancel_rl.setOnClickListener(onClickListener);
        dialog_confirm_rl.setOnClickListener(onClickListener);

        noticeDialog.show();

    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();

        if (noticeDialog != null && noticeDialog.isShowing()) {
            noticeDialog.dismiss();
        }
        if (waitDialog != null && waitDialog.isShowing()) {
            waitDialog.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        //销毁线程
//		mHandler.removeCallbacks(mBackgroundRunnable);
    }
}
