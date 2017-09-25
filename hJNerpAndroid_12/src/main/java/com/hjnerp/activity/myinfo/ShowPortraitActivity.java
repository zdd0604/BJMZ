package com.hjnerp.activity.myinfo;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hjnerp.common.ActionBarWidgetActivity;
import com.hjnerp.common.Constant;
import com.hjnerp.net.ChatConstants;
import com.hjnerp.net.ChatPacketHelper;
import com.hjnerp.util.ImageLoaderHelper;
import com.hjnerp.util.StringUtil;
import com.hjnerpandroid.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowPortraitActivity extends ActionBarWidgetActivity implements View.OnClickListener {
    @BindView(R.id.portraitImg)
    ImageView portraitImg;
    @BindView(R.id.action_left_tv)
    TextView actionLeftTv;
    @BindView(R.id.action_center_tv)
    TextView actionCenterTv;
    @BindView(R.id.action_right_tv)
    TextView actionRightTv;
    private String photoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_portrait);
        ButterKnife.bind(this);
        initView();
    }

    public void initView() {
        actionCenterTv.setText(getString(R.string.MyInfor_Title_PhotoPortrait));
        actionRightTv.setVisibility(View.GONE);
        portraitImg.setOnClickListener(this);
        actionLeftTv.setOnClickListener(this);
        photoUrl = getIntent().getStringExtra("photoUrl");

        if (StringUtil.isStrTrue(photoUrl))
            ImageLoaderHelper.displayImage(ChatPacketHelper.buildImageRequestURL(photoUrl,
                    ChatConstants.iq.DATA_VALUE_RES_TYPE_ATTACH), portraitImg);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_left_tv:
                finish();
                break;
        }
    }
}
