package com.mznerp.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.mznerp.activity.adapter.HjABoutAdapter;
import com.mznerp.common.ActionBarWidgetActivity;
import com.mznerp.common.Constant;
import com.mznerp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HJAboutInfoActivity extends ActionBarWidgetActivity implements View.OnClickListener {
    private int title;
    private HjABoutAdapter hjABoutAdapter;
    @BindView(R.id.action_left_tv)
    TextView actionLeftTv;
    @BindView(R.id.action_center_tv)
    TextView actionCenterTv;
    @BindView(R.id.action_right_tv)
    TextView actionRightTv;
    @BindView(R.id.action_right_tv1)
    TextView actionRightTv1;
    @BindView(R.id.hj_about)
    ListView hj_about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hj_about_info);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        Bundle bundle = this.getIntent().getExtras();
        title = bundle.getInt("title");
        actionLeftTv.setOnClickListener(this);
        actionRightTv.setVisibility(View.GONE);
        actionCenterTv.setText(title);
        hjABoutAdapter = new HjABoutAdapter(HJAboutInfoActivity.this, Constant.HJbean);
        hjABoutAdapter.notifyDataSetChanged();
        hj_about.setAdapter(hjABoutAdapter);
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
