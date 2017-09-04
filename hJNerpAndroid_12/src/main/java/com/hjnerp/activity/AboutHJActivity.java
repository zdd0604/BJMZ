package com.hjnerp.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hjnerp.common.ActivitySupport;
import com.hjnerp.common.Constant;
import com.hjnerp.model.HJAboutDate;
import com.hjnerp.util.SharePreferenceUtil;
import com.hjnerpandroid.R;


public class AboutHJActivity extends ActivitySupport implements View.OnClickListener {
    //关于和佳
    private RelativeLayout linear_information;
    //服务协议
    private RelativeLayout linear_sever;
    //隐私政策
    private RelativeLayout linear_policy;
    //版权信息
    private RelativeLayout linear_copyright;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setTitle("关于");
        setContentView(R.layout.about_hj);

        initView();

    }

    private void initView() {

        linear_information = (RelativeLayout) findViewById(R.id.linear_information);
        linear_information.setOnClickListener(this);

        linear_sever = (RelativeLayout) findViewById(R.id.linear_sever);
        linear_sever.setOnClickListener(this);

        linear_policy = (RelativeLayout) findViewById(R.id.linear_policy);
        linear_policy.setOnClickListener(this);

        linear_copyright = (RelativeLayout) findViewById(R.id.linear_copyright);
        linear_copyright.setOnClickListener(this);

        TextView txtViewOs = (TextView) findViewById(R.id.tv_abouthj_system);
        txtViewOs.setText("系统版本：" + SharePreferenceUtil.getInstance(this.getContext()).getOsType());
        TextView txtViewapp = (TextView) findViewById(R.id.tv_abouthj_app);
        txtViewapp.setText("和佳ERP版本：" + SharePreferenceUtil.getInstance(this.getContext()).getAppVersion());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linear_sever:
                intentActivity(R.string.about_hjsever,HJAboutInfoActivity.class);
                Constant.HJbean = HJAboutDate.getGSJJ();
                break;
            case R.id.linear_policy:
                intentActivity(R.string.about_hjpolicy,HJAboutInfoActivity.class);
                Constant.HJbean = HJAboutDate.getFZLC();
                break;
            case R.id.linear_copyright:
                intentActivity(R.string.about_hjcopyright,HJAboutInfoActivity.class);
                Constant.HJbean = HJAboutDate.getGSRY();
                break;
            case R.id.linear_information:
                intentActivity(R.string.about_hjinfor,HJCallPhoneActivity.class);
                break;
        }
    }

//	private void intentActivity(int title, String weburl) {
//		Intent intent = new Intent(AboutHJActivity.this, HjAboutInformation.class);
//		Bundle bundle = new Bundle();
//		bundle.putInt("title", title);
//		bundle.putCharSequence("weburl", weburl);
//		intent.putExtras(bundle);
//		startActivity(intent);
//	}

    private void intentActivity(int title,Class c) {
        Intent intent = new Intent(AboutHJActivity.this, c);
        Bundle bundle = new Bundle();
        bundle.putInt("title", title);
        intent.putExtras(bundle);
        startActivity(intent);
    }


}
