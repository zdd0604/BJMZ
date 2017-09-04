package com.hjnerp.activity.work;

import android.app.Dialog;
import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hjnerp.activity.work.adapter.WorkFlowRecorderInfoAdapter;
import com.hjnerp.common.ActivitySupport;
import com.hjnerp.common.Constant;
import com.hjnerp.common.EapApplication;
import com.hjnerp.dao.QiXinBaseDao;
import com.hjnerp.dao.WorkFlowBaseDao;
import com.hjnerp.fragment.WorkFragment;
import com.hjnerp.model.UserInfo;
import com.hjnerp.model.WorkflowApproveInfo;
import com.hjnerp.model.WorkflowApproveResp;
import com.hjnerp.model.WorkflowDetailInfo;
import com.hjnerp.model.WorkflowDetailResp;
import com.hjnerp.model.WorkflowListInfo;
import com.hjnerp.model.WorkflowRemarkResp;
import com.hjnerp.net.ChatConstants;
import com.hjnerp.net.HttpClientManager;
import com.hjnerp.net.HttpClientManager.HttpResponseHandler;
import com.hjnerp.util.AttachmentFileReader;
import com.hjnerp.util.StringUtil;
import com.hjnerp.util.ToastUtil;
import com.hjnerp.widget.WaitDialogRectangle;
import com.hjnerpandroid.R;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApprovalActivity extends ActivitySupport {
    private String TAG = "ApprovalActivity";

    private ImageView photo;
    private Button agree, disagree, withdraw;
    private EditText et_remark, et_withdraw;
    private RelativeLayout approvalLayout, withdrawLayout;
    //	private ListView listview;
    private PullToRefreshListView listview;
    private WorkFlowRecorderInfoAdapter listItemAdapter;
    private List<WorkflowApproveInfo> workflowApproveList = new ArrayList<WorkflowApproveInfo>();// 工单审批流程
    private List<Map<String, List<WorkflowDetailInfo>>> tables;// 工单详情
    private Thread getAttachThread;
    private WorkflowListInfo wfInfo;
    protected UserInfo myInfo;//我的用户表信息

    /**
     * 审核评论是否同步标志
     */
    // private boolean isSynchronized = false;
    private WaitDialogRectangle waitDialog;
    private Dialog noticeDialog;
    private WaitDialogRectangle waitDialogText;
    private String remark;//审批附加意见
    private String action;//用户的动作（同意、不同意、收回）

    private String errormsg;
    private final static int GET_DETAIL_OK = 1;
    private final static int GET_DETAIL_ERROE = 2;
    private final static int GET_APPROVAL_OK = 3;
    private final static int GET_APPROVAL_ERROR = 4;

    private final static int APPROVAL_DONE = 5;
    private boolean getdetail_flag = false;
    private boolean getapproval_flag = false;
    //	private TimerThread timerThread = null;
    private Thread mThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.approval);
        mActionBar = this.getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        /** 获得WorkflowInfo对象 */
        Intent intent = getIntent();
        Bundle mBundle = intent.getExtras();
        wfInfo = (WorkflowListInfo) mBundle.getSerializable(Constant.MY_NEWS);
        mActionBar.setTitle("审批单详情");
        //	Log.i(TAG,">>>>>>>>>>>>>>>>> workinfo billNo:" + wfInfo.getBillNo() + " billType:" + wfInfo.getBillType() + " optType:" + wfInfo.getOptType() );
        myInfo = QiXinBaseDao.queryCurrentUserInfo();

        waitDialog = new WaitDialogRectangle(this);
        waitDialogText = new WaitDialogRectangle(this);
        findView();
        closeInput();

        // 未处理标志显示驳回和同意的按钮,已处理的界面显示收回按钮
        hideApprovalLayout(wfInfo, wfInfo.getFlagDeal().equalsIgnoreCase("N") ? false : true);

        /** 审批过程的处理 */
        listview = (PullToRefreshListView) findViewById(R.id.plv_approval);
        /** 获取审批流程 */
        getWorkflowProcedure(wfInfo.getUser().getComID(), wfInfo.getBillNo(), wfInfo.getBillType());
        getWorkFlowDetail(wfInfo.getUser().getComID(), wfInfo.getBillNo(), wfInfo.getBillType());

        // list.addAll(queryNativeWorkDetailInfo(wfInfo.getBillNo(),
        // wfInfo.getBillType()));
        listItemAdapter = new WorkFlowRecorderInfoAdapter(this,
                workflowApproveList, wfInfo, tables);
        listview.setAdapter(listItemAdapter);

    }

    private OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View arg0) {
            switch (arg0.getId()) {
                case R.id.btn_agree:
                    remark = et_remark.getText().toString();
                    action = Constant.WF_OP_AGREE;
                    //Log.e(TAG, "同意  " + remark);
                    sendBillAction(action, remark);

                    break;
                case R.id.btn_disagree:
                    remark = et_remark.getText().toString();
                    if (StringUtil.isNullOrEmpty(remark)) {
                        ToastUtil.ShowShort(ApprovalActivity.this, "驳回意见不能为空");
                    } else {
                        action = Constant.WF_OP_REJEST_ONE;
                        //	Log.e(TAG, "不同意  " + remark);
                        sendBillAction(action, remark);
                    }
                    break;
                case R.id.btn_withdraw://如果有后续审批人，不能收回并给提示
                    // TODO
//				if (ifCanRevoke()) {
                    action = Constant.WF_OP_REVOKE;
                    remark = et_withdraw.getText().toString();
                    sendBillAction(action, remark);
//				} else {
//					showNoticeDialog("该单据有后续审批人，无法收回");
//				}
                    break;
                default:
                    break;
            }

        }
    };


    private void refreshApproval() {
        hideApprovalLayout(wfInfo, wfInfo.getFlagDeal().equalsIgnoreCase("N") ? false
                : true);
        listItemAdapter.refreshList(workflowApproveList, wfInfo, tables);
    }

    /**
     * 显示已处理界面还是未处理界面
     */
    private void hideApprovalLayout(WorkflowListInfo wfInfo, boolean needHided) {
        if (needHided) {//单据已处理
            approvalLayout.setVisibility(View.GONE);
            //withdrawLayout.setVisibility(View.VISIBLE);

            if ("R".equals(wfInfo.getOptType())) {//审阅单
                withdrawLayout.setVisibility(View.GONE);
//				withdrawLayout.setVisibility(View.VISIBLE);
            } else if ("Y".equals(wfInfo.getOptType())) {//审批单
                withdrawLayout.setVisibility(View.VISIBLE);
            } else if ("A".equals(wfInfo.getOptType())) {//会签
                withdrawLayout.setVisibility(View.GONE);
            }

        } else {//单据未处理
            withdrawLayout.setVisibility(View.GONE);
            //approvalLayout.setVisibility(View.VISIBLE);

            if ("R".equals(wfInfo.getOptType())) {//审阅单
                //Log.i(TAG,"这是个未处理的审阅单");
                approvalLayout.setVisibility(View.VISIBLE);
                agree.setVisibility(View.VISIBLE);
                disagree.setVisibility(View.GONE);
            } else if ("Y".equals(wfInfo.getOptType())) {//审批单
                approvalLayout.setVisibility(View.VISIBLE);
            } else if ("A".equals(wfInfo.getOptType())) {
                approvalLayout.setVisibility(View.VISIBLE);
                disagree.setVisibility(View.GONE);
            }


        }
    }

    /**
     * 初始化view
     */
    private void findView() {
        et_remark = (EditText) findViewById(R.id.et_approval_context);
        et_withdraw = (EditText) findViewById(R.id.et_withdraw_context);
        agree = (Button) findViewById(R.id.btn_agree);
        disagree = (Button) findViewById(R.id.btn_disagree);
        withdraw = (Button) findViewById(R.id.btn_withdraw);
        withdrawLayout = (RelativeLayout) findViewById(R.id.approval_withdraw);
        approvalLayout = (RelativeLayout) findViewById(R.id.approval_approval);

        agree.setOnClickListener(onClickListener);
        disagree.setOnClickListener(onClickListener);
        withdraw.setOnClickListener(onClickListener);

    }

    private void sendToMyHandler(int msg) {
        Message Msg = new Message();
        Bundle b = new Bundle();
        b.putInt("flag", msg);
        Msg.setData(b);

        myHandler.sendMessage(Msg);
    }

    final Handler myHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            Bundle b = msg.getData();
            int mmsg = b.getInt("flag");
            Log.i(TAG, " handler mmsg is " + mmsg);
            switch (mmsg) {
                case GET_DETAIL_OK:
                case GET_APPROVAL_OK:

                    if (getapproval_flag && getdetail_flag) {
                        if (waitDialog.isShowing())
                            waitDialog.dismiss();
                        refreshApproval();
                    }
                    break;
                case APPROVAL_DONE:
                    waitDialogText.dismiss();
//				timerThread = null;
                    if (action.equals(Constant.WF_OP_REVOKE)) {
                        setResult(33);//驳回
                    } else {
                        setResult(22);
                    }
                    finish();

                    break;
                default:
                    break;
            }

        }

        ;
    };

    // 获取附件，召唤神兽打开
    public void getAttach(final String comID, final String billType, final String attachId, final String attachName) {
        waitDialog.show();
        getAttachThread = new Thread() {
            @Override
            public void run() {
                final File file = new File(Environment.getExternalStorageDirectory(), attachName);
                AndroidHttpClient httpClient = AndroidHttpClient
                        .newInstance("attachmentLoader");
                try {
                    HttpPost httpPost = WorkFragment.postWorkflow(Constant.WF_TYPE_ATTACH, comID, null, billType, null, null, null, null, null, attachId);
                    HttpResponse httpResponse = httpClient.execute(httpPost);
                    int statusCode = httpResponse.getStatusLine().getStatusCode();
                    if (HttpStatus.SC_OK == statusCode) {
                        waitDialog.dismiss();
                        if ("0".equals(httpResponse.getFirstHeader(
                                ChatConstants.iq.HEAD_KEY_DOWNLOADABLE_STATUS)
                                .getValue())) {// 成功
                            httpResponse.getEntity().writeTo(
                                    new FileOutputStream(file));
                            httpClient.close();

                            ((Handler) EapApplication
                                    .getApplication()
                                    .getExtra(EapApplication.EXTRA_MAIN_HANDLER))
                                    .post(new Runnable() {
                                        @Override
                                        public void run() {
                                            AttachmentFileReader
                                                    .read(ApprovalActivity.this,
                                                            file);
                                        }
                                    });
                        }
                    }
                } catch (Exception e) {
                    waitDialog.dismiss();
                    e.printStackTrace();
                }
                httpClient.close();
            }
        };
        getAttachThread.start();
    }

    // 获取工单详情
    private void getWorkFlowDetail(final String comID, final String billNo, final String billType) {
        if (!waitDialog.isShowing()) {
            waitDialog.show();
        }
        HttpPost post = WorkFragment.postWorkflow(Constant.WF_TYPE_DETAIL, comID,
                billNo, billType, null, null, null, null, null, null);
        if (post == null)
            return;
        HttpClientManager.addTask(new HttpResponseHandler() {
            @Override
            public void onResponse(HttpResponse resp) {
                try {
                    String msg = HttpClientManager.toStringContent(resp);
                    Log.i(TAG, "http请求,获取工单详情.getWorkFlowDetail string is >>>>>>> " + msg);
                    if (TextUtils.isEmpty(msg) || msg.contains("error")) {
                        Log.e(TAG, "getWorkFlowDetail error!");
                        errorHandle("获取审批流程失败，请重试！");
                    } else if (msg.contains("session")) {//sesson无效
                        errorForceHandle();
                    } else {
                        Pattern p = Pattern.compile("\\d+\\.\\d+");
                        Matcher m = p.matcher(msg);
                        StringBuffer sb = new StringBuffer();
                        while (m.find()) {
                            Double d = Double.parseDouble(m.group());
                            String a = String.format("%.2f", d);
                            m.appendReplacement(sb, a);
                        }
                        m.appendTail(sb);
                        msg = sb.toString();
                        Gson gson = new Gson();
                        WorkflowDetailResp workflowResp = gson.fromJson(msg,
                                WorkflowDetailResp.class);
                        if ("result".equalsIgnoreCase(workflowResp.type) && workflowResp.data != null) {
                            getdetail_flag = true;
                            tables = workflowResp.data.tables;
                            sendToMyHandler(GET_DETAIL_OK);

                        } else {
                            errorHandle("获取审批流程失败，请重试！");
                        }
                    }
                } catch (IOException e) {
                    onException(e);
                }
            }

            @Override
            public void onException(Exception e) {
                e.printStackTrace();
            }
        }, post);
    }

    private void errorHandle(final String msg) {
        ((Handler) EapApplication.getApplication().getExtra(
                EapApplication.EXTRA_MAIN_HANDLER))
                .post(new Runnable() {
                    @Override
                    public void run() {
                        if (waitDialog != null && waitDialog.isShowing()) {
                            waitDialog.dismiss();
                        }
//						isForcedExit();
                        ToastUtil.ShowShort(ApprovalActivity.this, msg);
                        finish();
                    }
                });
    }

    private void errorForceHandle() {
        ((Handler) EapApplication.getApplication().getExtra(
                EapApplication.EXTRA_MAIN_HANDLER))
                .post(new Runnable() {
                    @Override
                    public void run() {
                        if (waitDialog != null && waitDialog.isShowing()) {
                            waitDialog.dismiss();
                        }
                        isForcedExit(ChatConstants.error_string.ERROR_STRING_SESSION_INVALID);
                    }
                });
    }

    // 获取审批流程
    private final void getWorkflowProcedure(final String comID, final String billNo,
                                            final String billType) {
        if (!waitDialog.isShowing()) {
            waitDialog.show();
        }
        HttpPost post = WorkFragment.postWorkflow(Constant.WF_TYPE_PROCEDURE, comID,
                billNo, billType, null, null, null, null, null, null);
        if (post == null)
            return;
        HttpClientManager.addTask(new HttpResponseHandler() {
            @Override
            public void onResponse(HttpResponse resp) {
                try {

                    String msg = HttpClientManager.toStringContent(resp);
                    Log.i(TAG, "http请求,获取审批流程.getWorkflowProcedure string is >>>>>>> " + msg);
//					if(TimeKey.isPasttime()){
//						msg = null;
//					}
                    if (TextUtils.isEmpty(msg) || msg.contains("error")) {
                        Log.e(TAG, "getWorkflowProcedure error!");
                        errorHandle("获取审批流程失败，请重试！");
                    } else if (msg.contains("session")) {//sesson无效
                        errorForceHandle();
                    } else {
                        Gson gson = new Gson();
                        WorkflowApproveResp workflowResp = gson.fromJson(msg,
                                WorkflowApproveResp.class);
                        if ("result".equalsIgnoreCase(workflowResp.type) && workflowResp.data != null) {
                            workflowApproveList = new ArrayList<WorkflowApproveInfo>();
                            WorkflowApproveInfo approveInfo = new WorkflowApproveInfo();
                            workflowApproveList.add(approveInfo);

                            workflowApproveList.addAll(workflowResp.data.items);
                            getapproval_flag = true;
                            sendToMyHandler(GET_APPROVAL_OK);

                        } else {
                            errorHandle("获取审批流程失败，请重试！");
                        }

                    }
                } catch (IOException e) {
                    onException(e);
                }
            }

            @Override
            public void onException(Exception e) {
                e.printStackTrace();
            }
        }, post);
    }

    /**
     * @param remark   审核意见
     * @param dealType 审核类型 send ，rejest ，revoke
     */
    private void sendBillAction(String dealType, String remark) {
        waitDialog.show();
        HttpPost post = WorkFragment.postWorkflow(Constant.WF_TYPE_OPERATE, wfInfo.getUser().getComID(),
                wfInfo.getBillNo(), wfInfo.getBillType(), null, null, null,
                dealType, remark, null);
        if (post == null)
            return;
        HttpClientManager.addTask(new HttpResponseHandler() {
            @Override
            public void onResponse(HttpResponse resp) {
                try {
                    String msg = HttpClientManager.toStringContent(resp);
                    Log.i(TAG, "sendBillAction response is " + msg);
                    Gson gson = new Gson();
                    WorkflowRemarkResp workflowResp = gson.fromJson(msg,
                            WorkflowRemarkResp.class);

                    if (workflowResp == null) {
                        ((Handler) EapApplication.getApplication().getExtra(
                                EapApplication.EXTRA_MAIN_HANDLER))
                                .post(new Runnable() {
                                    @Override
                                    public void run() {
                                        waitDialog.dismiss();
                                    }
                                });
                        ToastUtil.ShowShort(ApprovalActivity.this, "网络错误，请重试！");
                        return;
                    }
                    if ("result".equalsIgnoreCase(workflowResp.type)) {
                        // TODO
                        final String dealperson = workflowResp.data.msg;
                        if (action.equals(Constant.WF_OP_AGREE)) {
                            wfInfo.setFlagDeal("Y");

                        } else if (action.equals(Constant.WF_OP_REJEST_ONE)) {
                            wfInfo.setFlagDeal("Y");

                        } else if (action.equals(Constant.WF_OP_REVOKE)) {
                            wfInfo.setFlagDeal("N");

                        }
                        WorkFlowBaseDao.replaceWorkFlowInfo(wfInfo);

                        ///审批成功，
                        //	getWorkflowProcedure(wfInfo.getUser().getComID(), wfInfo.getBillNo(),
                        //			wfInfo.getBillType());

                        ((Handler) EapApplication.getApplication().getExtra(
                                EapApplication.EXTRA_MAIN_HANDLER))
                                .post(new Runnable() {
                                    @Override
                                    public void run() {
                                        waitDialog.dismiss();
                                    }
                                });

                        //TODO 成功后弹出成功提示，然后自动消除弹窗返回上一个界面

                        ((Handler) EapApplication.getApplication().getExtra(
                                EapApplication.EXTRA_MAIN_HANDLER))
                                .post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (waitDialogText == null) {
                                            waitDialogText = new WaitDialogRectangle(ApprovalActivity.this);

                                        }
                                        waitDialogText.show();

                                        if (action.equals(Constant.WF_OP_AGREE)) {
                                            waitDialogText.setText("审核成功！ ");

                                        } else if (action.equals(Constant.WF_OP_REJEST_ONE)) {
                                            waitDialogText.setText("驳回成功！ ");

                                        } else if (action.equals(Constant.WF_OP_REVOKE)) {
                                            waitDialogText.setText("收回成功！ ");
                                        }

                                    }
                                });


                        waitThread();
                    } else {
                        errormsg = workflowResp.data.msg;
                        ((Handler) EapApplication.getApplication().getExtra(
                                EapApplication.EXTRA_MAIN_HANDLER))
                                .post(new Runnable() {
                                    @Override
                                    public void run() {
                                        waitDialog.dismiss();
                                    }
                                });

                        ((Handler) EapApplication.getApplication().getExtra(
                                EapApplication.EXTRA_MAIN_HANDLER))
                                .post(new Runnable() {
                                    @Override
                                    public void run() {
                                        showNoticeDialog(errormsg);
                                    }
                                });

                    }


                } catch (IOException e) {
                    onException(e);
                }
            }

            @Override
            public void onException(Exception e) {
                e.printStackTrace();
            }
        }, post);
    }

    public void showNoticeDialog(String msg) {
        noticeDialog = new Dialog(this, R.style.noticeDialogStyle);
        noticeDialog.setContentView(R.layout.dialog_notice_nocancel);

        TextView notice = (TextView) noticeDialog
                .findViewById(R.id.nc_notice);
        notice.setText(msg);

        RelativeLayout dialog_confirm_rl = (RelativeLayout) noticeDialog
                .findViewById(R.id.dialog_nc_confirm_rl);

        dialog_confirm_rl.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                noticeDialog.dismiss();
            }
        });

        noticeDialog.show();

    }


    private void waitThread() {
        mThread = new Thread() {
            @Override
            public void run() {

                try {
                    sleep(2000);
                    sendToMyHandler(APPROVAL_DONE);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };
        mThread.start();
    }

}
