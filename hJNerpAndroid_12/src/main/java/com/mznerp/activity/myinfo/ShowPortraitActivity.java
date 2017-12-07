package com.mznerp.activity.myinfo;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mznerp.common.ActionBarWidgetActivity;
import com.mznerp.net.ChatConstants;
import com.mznerp.net.ChatPacketHelper;
import com.mznerp.util.ImageLoaderHelper;
import com.mznerp.util.StringUtil;
import com.mznerp.R;

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
