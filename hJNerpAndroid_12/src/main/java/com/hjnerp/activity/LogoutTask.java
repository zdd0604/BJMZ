package com.hjnerp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.hjnerp.common.ActivitySupport;
import com.hjnerp.common.Constant;
import com.hjnerp.dao.QiXinBaseDao;
import com.hjnerp.db.Tables;
import com.hjnerp.manager.HJWebSocketManager;
import com.hjnerp.net.ChatPacketHelper;
import com.hjnerp.net.IQ;
import com.hjnerp.util.ToastUtil;
import com.hjnerp.widget.WaitDialogRectangle;

public class LogoutTask extends AsyncTask<String, Integer, Integer> {
    private ActivitySupport activitySupport;
    private Context context;
    private String errorText;
    private WaitDialogRectangle waitDialogRectangle;

    public LogoutTask(ActivitySupport activitySupport) {

        this.activitySupport = activitySupport;
        this.context = activitySupport.getContext();
        this.waitDialogRectangle = activitySupport.getWaitDialogRectangle();
    }

    @Override
    protected void onPreExecute() {
        waitDialogRectangle.show();
        waitDialogRectangle.setText("正在退出...");
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(String... params) {
        return logout();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
    }

    @Override
    protected void onPostExecute(Integer result) {
        switch (result) {
            case Constant.LOGIN_SECCESS: // 登录成功
                ActivitySupport.sputil.setForceExit(false);
                ActivitySupport.sputil.setMySessionId("");
                QiXinBaseDao.updateUserInfo(
                        ActivitySupport.sputil.getMyUserId(),
                        Tables.UserTable.COL_VAR_SESSION,
                        "");
                MainActivity.newMain.finish();
                context.startActivity(new Intent(context, LoginActivity.class));
                waitDialogRectangle.dismiss();
                ((Activity) activitySupport).finish();

                break;
            case Constant.LOGIN_ERROR_ACCOUNT_PASS:// 账户或者密码错误
                ToastUtil.ShowShort(context, errorText);
                break;
            case Constant.SERVER_UNAVAILABLE:// 服务器连接失败
                ToastUtil.ShowShort(context, errorText);
                break;
            case Constant.LOGIN_ERROR:// 未知异常
                ToastUtil.ShowShort(context, errorText);
                break;
            case Constant.LOGIN_ANONYMOUS:
                // activitySupport.startService();

        }
        super.onPostExecute(result);

    }

    private Integer logout() {
        activitySupport.stopService();
        errorText = "";
        IQ requestiq = HJWebSocketManager.getInstance().logout();
        ChatPacketHelper.parseErrorCode(requestiq);


        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        SharedPreferences sharedPref = MainActivity.newMain.getSharedPreferences("main",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("isNoti", 0);
        editor.commit();
        MainActivity.newMain.finish();
        return Constant.LOGIN_SECCESS;
    }

}
