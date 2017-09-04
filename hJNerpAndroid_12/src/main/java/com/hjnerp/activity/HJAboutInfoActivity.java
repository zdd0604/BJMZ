package com.hjnerp.activity;

import android.os.Bundle;
import android.widget.ListView;

import com.hjnerp.activity.adapter.HjABoutAdapter;
import com.hjnerp.common.ActivitySupport;
import com.hjnerp.common.Constant;
import com.hjnerp.model.HJAboutBean;
import com.hjnerpandroid.R;

import java.util.List;

public class HJAboutInfoActivity extends ActivitySupport {
    private ListView hj_about;
    private int title;
    private HjABoutAdapter hjABoutAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_hj_about_info);

        Bundle bundle = this.getIntent().getExtras();
        title = bundle.getInt("title");
        mActionBar.setTitle(title);

        initView();
    }

    private void initView() {
        hj_about = (ListView) findViewById(R.id.hj_about);
        hjABoutAdapter = new HjABoutAdapter(HJAboutInfoActivity.this, Constant.HJbean);
        hjABoutAdapter.notifyDataSetChanged();
        hj_about.setAdapter(hjABoutAdapter);
    }
}
