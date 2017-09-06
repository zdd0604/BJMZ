package com.hjnerp.common;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hjnerp.model.LoginConfig;
import com.hjnerp.widget.WaitDialogRectangle;
import com.hjnerpandroid.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Admin on 2017/8/31.
 */

public class ActionBarWidgetActivity extends AppCompatActivity implements
        IActivitySupport {
    protected Context mContext;
    //弹框
    protected WaitDialogRectangle waitDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    /**
     * 创建部分实体
     */
    private void initView() {
        mContext = this;
        waitDialog = new WaitDialogRectangle(mContext);
    }


    /**
     * 长toast
     *
     * @param content
     */
    public void toastLONG(String content) {
        Toast.makeText(mContext, content, Toast.LENGTH_LONG).show();
    }

    /**
     * 短toast
     *
     * @param content
     */
    public void toastSHORT(String content) {
        Toast.makeText(mContext, content, Toast.LENGTH_SHORT).show();
    }

    /**
     * 短toast
     *
     * @param content
     */
    public void LogShow(String content) {
        Log.e("MZ", content);
    }


    /**
     * bundle
     *
     * @param from
     * @param to
     */
    public void intentActivity(Context from, Class to, Bundle bundle) {
        Intent intent = new Intent(from, to);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public EapApplication getEapApplication() {
        return null;
    }

    @Override
    public void stopService() {

    }

    @Override
    public void startService() {

    }

    @Override
    public boolean validateInternet() {
        return false;
    }

    @Override
    public boolean hasInternetConnected() {
        ConnectivityManager manager = (ConnectivityManager) mContext
                .getSystemService(mContext.CONNECTIVITY_SERVICE);
        if (manager != null) {
            NetworkInfo network = manager.getActiveNetworkInfo();
            if (network != null && network.isConnectedOrConnecting()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void isExit() {

    }

    @Override
    public boolean hasLocationGPS() {
        return false;
    }

    @Override
    public boolean hasLocationNetWork() {
        return false;
    }

    @Override
    public void checkMemoryCard() {

    }

    @Override
    public ProgressDialog getProgressDialog() {
        return null;
    }

    @Override
    public WaitDialogRectangle getWaitDialogRectangle() {
        return null;
    }

    @Override
    public Context getContext() {
        return null;
    }

    @Override
    public void saveLoginConfig(LoginConfig loginConfig) {

    }

    @Override
    public LoginConfig getLoginConfig() {
        return null;
    }

    @Override
    public void setNotiType(int iconId, String contentTitle, String contentText, Class activity, String from) {

    }
}
