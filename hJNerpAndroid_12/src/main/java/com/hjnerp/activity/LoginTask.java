package com.hjnerp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.hjnerp.business.Ctlm1345Update;
import com.hjnerp.business.Ctlm1347Update;
import com.hjnerp.common.Constant;
import com.hjnerp.common.IActivitySupport;
import com.hjnerp.dao.QiXinBaseDao;
import com.hjnerp.manager.HJWebSocketManager;
import com.hjnerp.model.FriendInfo;
import com.hjnerp.model.GroupInfo;
import com.hjnerp.model.LoginConfig;
import com.hjnerp.model.UserInfo;
import com.hjnerp.net.ChatConstants;
import com.hjnerp.net.ChatPacketHelper;
import com.hjnerp.net.IQ;
import com.hjnerp.util.SharePreferenceUtil;
import com.hjnerp.util.ToastUtil;
import com.hjnerp.widget.WaitDialogRectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LoginTask extends AsyncTask<String, Integer, Integer> {
    private IActivitySupport activitySupport;
    private WaitDialogRectangle waitDialogRectangle;
    private Context context;
    private LoginConfig loginConfig;
    private String errorText;
    protected SharePreferenceUtil sputil;

    public LoginTask(IActivitySupport activitySupport, LoginConfig loginConfig,
                     SharePreferenceUtil spUtil) {

        this.activitySupport = activitySupport;
        this.loginConfig = loginConfig;
        this.waitDialogRectangle = activitySupport.getWaitDialogRectangle();
        this.context = activitySupport.getContext();
        this.sputil = spUtil;
    }

    @Override
    protected void onPreExecute() {
        waitDialogRectangle.show();
        waitDialogRectangle.setText("正在登录...");
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(String... params) {
        return login();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
    }

    @Override
    protected void onPostExecute(Integer result) {
        switch (result) {
            case -1: // 登录成功
                waitDialogRectangle.dismiss();
                ToastUtil.ShowShort(context, "服务器没有反馈，请查看手机网络设置。");
                break;
            case Constant.LOGIN_SECCESS: // 登录成功

                new Ctlm1347Update(null).action();
                //
                new Ctlm1345Update(new String[]{"ctlm1345"},
                        new String[]{"1=1"}, null).action();
                waitDialogRectangle.dismiss();
                context.startActivity(new Intent(context, MainActivity.class));
                ((Activity) activitySupport).finish();

                // }
                break;
            case Constant.LOGIN_ERROR_ACCOUNT_PASS:// 账户或者密码错误
                waitDialogRectangle.dismiss();
                ToastUtil.ShowShort(context, errorText);
                Log.v("s", errorText);
                break;
            case Constant.SERVER_UNAVAILABLE:// 服务器连接失败
                waitDialogRectangle.dismiss();
                ToastUtil.ShowShort(context, errorText);
            case Constant.LOGIN_ERROR:// 未知异常
                waitDialogRectangle.dismiss();
                ToastUtil.ShowShort(context, errorText);
                break;
        }
        super.onPostExecute(result);

    }

    @SuppressWarnings("unchecked")
    private Integer login() {
        try {
            IQ iq = HJWebSocketManager.getInstance().login(
                    loginConfig.getComid(), loginConfig.getUsername(),
                    loginConfig.getPassword());
            if (iq == null) {
                return -1;
            }
            if (ChatConstants.iq.TYPE_ERROR.equals(iq.type)) {
                errorText = ChatPacketHelper.parseErrorCode(iq);
                return Constant.LOGIN_ERROR_ACCOUNT_PASS;
            } else {
                Map<String, String> dataMap = (Map<String, String>) iq.data;
                UserInfo ui = ChatPacketHelper.createUserInfo(dataMap);
                sputil.setMySessionId(ui.sessionID);
                sputil.setComid(ui.companyID);
                sputil.setComName(ui.companyName);
                sputil.setMyUserId(ui.userID);
                sputil.setMyUserName(ui.username);
                sputil.setUserImager(ui.userImage);

                sputil.setForceExit(true);
                QiXinBaseDao.replaceUserInfo(ui);
            }

            // 取联系人
            IQ iqs = HJWebSocketManager.getInstance().requestIQ(
                    ChatConstants.iq.FEATURE_REQUEST_CONTACT);
            if (iqs != null) {
                if (ChatConstants.iq.TYPE_SUCCESS.equals(iqs.type)) {
                    Map<String, Object> dataMap = (Map<String, Object>) iqs.data;
                    List<Map<String, String>> items = (List<Map<String, String>>) dataMap
                            .get(ChatConstants.iq.DATA_KEY_ITEMS);
                    ArrayList<FriendInfo> list = new ArrayList<FriendInfo>();
                    for (Map<String, String> item : items) {
                        list.add(ChatPacketHelper.createFriendInfo(item));
                    }
                    // 清除联系人
                    QiXinBaseDao.replaceFriendInfos(list, 'Y');
                }
            }
            // /群组信息
            IQ iqgroupinfo = HJWebSocketManager.getInstance().requestIQ(
                    ChatConstants.iq.FEATURE_REQUEST_GROUP_INFO);
            if (iqgroupinfo != null) {
                if (ChatConstants.iq.TYPE_SUCCESS.equals(iqgroupinfo.type)) {

                    Map<String, Object> dataMap = (Map<String, Object>) iqgroupinfo.data;
                    Object obj = dataMap.get(ChatConstants.iq.DATA_KEY_ITEMS);

                    if (obj instanceof List) {
                        List<Map<String, String>> items = (List<Map<String, String>>) obj;
                        ArrayList<GroupInfo> list = new ArrayList<GroupInfo>();
                        for (Map<String, String> item : items)
                            list.add(ChatPacketHelper.createGroupInfo(item));
                        QiXinBaseDao.replaceGroupInfos(list);
                    }
                }
            }
            // 群组成员信息
            IQ iqgrouprelation = HJWebSocketManager.getInstance().requestIQ(
                    ChatConstants.iq.FEATURE_REQUEST_GROUP_RELATION);
            if (iqgrouprelation != null) {
                if (ChatConstants.iq.TYPE_SUCCESS.equals(iqgrouprelation.type)) {
                    Map<String, Object> dataMap = (Map<String, Object>) iqgrouprelation.data;
                    Object obj = dataMap.get(ChatConstants.iq.DATA_KEY_ITEMS);
                    if (obj instanceof List) {
                        List<Map<String, String>> items = (List<Map<String, String>>) obj;
                        QiXinBaseDao.insertGroupRelations(items);
                    }
                }

            }

            IQ iqgrouprelations = HJWebSocketManager.getInstance().requestIQ(
                    ChatConstants.iq.FEATURE_REQUEST_GROUP_RELATION);
            if (iqgrouprelation != null) {
                Map<String, Object> dataMap = (Map<String, Object>) iqgrouprelations.data;
                Object obj = dataMap.get(ChatConstants.iq.DATA_KEY_ITEMS);
                if (obj instanceof List) {
                    List<Map<String, String>> items = (List<Map<String, String>>) obj;
                    QiXinBaseDao.insertGroupRelations(items);
                }

                ArrayList<FriendInfo> mUserList = ChatPacketHelper
                        .processUnFriendsUsersInGroup(iqgrouprelations);
                QiXinBaseDao.replaceFriendInfos(mUserList, 'N');

            }

            // /获取是否有工作流权限单类型

            IQ iqworkFlow = HJWebSocketManager.getInstance().readyWorkFlow();
            if (iqworkFlow != null) {
                Map<String, Object> dataMap = (Map<String, Object>) iqworkFlow.data;
                Object obj = dataMap.get(ChatConstants.iq.DATA_KEY_CONTENT);
                if (obj != null) {

                    if ("Y".equalsIgnoreCase(obj.toString())) {
                        sputil.setWorkFlow(true);
                    } else {
                        sputil.setWorkFlow(false);
                    }
                }
            }
            return Constant.LOGIN_SECCESS;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            errorText = "服务器连接失败 ";
            return Constant.LOGIN_ERROR;
        }

    }

}
