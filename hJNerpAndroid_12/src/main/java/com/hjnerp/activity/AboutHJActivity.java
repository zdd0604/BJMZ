package com.hjnerp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hjnerp.common.ActionBarWidgetActivity;
import com.hjnerp.common.Constant;
import com.hjnerp.model.HJAboutDate;
import com.hjnerp.util.SharePreferenceUtil;
import com.hjnerpandroid.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AboutHJActivity extends ActionBarWidgetActivity implements View.OnClickListener {
    @BindView(R.id.action_left_tv)
    TextView actionLeftTv;
    @BindView(R.id.action_center_tv)
    TextView actionCenterTv;
    @BindView(R.id.action_right_tv)
    TextView actionRightTv;
    @BindView(R.id.action_right_tv1)
    TextView actionRightTv1;
    //关于和佳
    @BindView(R.id.linear_information)
    TextView linear_information;
    //服务协议
    @BindView(R.id.linear_sever)
    TextView linear_sever;
    //隐私政策
    @BindView(R.id.linear_policy)
    TextView linear_policy;
    //版权信息
    @BindView(R.id.linear_copyright)
    TextView linear_copyright;
    @BindView(R.id.tv_abouthj_system)
    TextView txtViewOs;
    @BindView(R.id.tv_abouthj_app)
    TextView txtViewapp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_hj);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        actionCenterTv.setText(getString(R.string.about_Title_TvActivity));
        actionRightTv.setVisibility(View.GONE);
        linear_information.setOnClickListener(this);
        actionLeftTv.setOnClickListener(this);
        linear_sever.setOnClickListener(this);
        linear_policy.setOnClickListener(this);
        linear_copyright.setOnClickListener(this);

//        txtViewOs.setText("系统版本：" + SharePreferenceUtil.getInstance(this.getContext()).getOsType());
        txtViewapp.setText("和佳软件：" + SharePreferenceUtil.getInstance(this.getContext()).getAppVersion());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linear_sever:
                intentActivity(R.string.about_Title_Hjsever, HJAboutInfoActivity.class);
                Constant.HJbean = HJAboutDate.getGSJJ();
                break;
            case R.id.linear_policy:
                intentActivity(R.string.about_Title_Hjpolicy, HJAboutInfoActivity.class);
                Constant.HJbean = HJAboutDate.getFZLC();
                break;
            case R.id.linear_copyright:
                intentActivity(R.string.about_Title_Hjcopyright, HJAboutInfoActivity.class);
                Constant.HJbean = HJAboutDate.getGSRY();
                break;
            case R.id.linear_information:
                intentActivity(R.string.about_Title_Hjinfor, HJCallPhoneActivity.class);
                break;
            case R.id.action_left_tv:
                finish();
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

    private void intentActivity(int title, Class c) {
        Intent intent = new Intent(AboutHJActivity.this, c);
        Bundle bundle = new Bundle();
        bundle.putInt("title", title);
        intent.putExtras(bundle);
        startActivity(intent);
    }


}
