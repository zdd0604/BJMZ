package com.hjnerp.business.activity;

import android.os.Bundle;

import com.hjnerp.common.ActivitySupport;
import com.hjnerpandroid.R;

public class AttendanceLocActivity extends ActivitySupport {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setTitle("位置信息");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.layout_hjattendance_info_activity); 
	}

		
}
