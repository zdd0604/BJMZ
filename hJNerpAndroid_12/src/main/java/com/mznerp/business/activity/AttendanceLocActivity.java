package com.mznerp.business.activity;

import android.os.Bundle;

import com.mznerp.common.ActivitySupport;
import com.mznerp.R;

public class AttendanceLocActivity extends ActivitySupport {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setTitle("位置信息");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.layout_hjattendance_info_activity); 
	}

		
}
