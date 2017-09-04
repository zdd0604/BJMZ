package com.hjnerp.activity.im;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.hjnerp.activity.im.adapter.ShowGroupListAdapter;
import com.hjnerp.common.ActivitySupport;
import com.hjnerp.common.Constant;
import com.hjnerp.dao.QiXinBaseDao;
import com.hjnerp.model.GroupInfo;
import com.hjnerpandroid.R;

import java.io.Serializable;
import java.util.ArrayList;

public class ShowGroupActivity extends ActivitySupport{
	private String TAG = "ShowGroupActivity";
	private ListView listView;
	private ShowGroupListAdapter adapter;
	private ArrayList<GroupInfo> groupInfoList;
	private RelativeLayout rl_actionbar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mActionBar = getSupportActionBar();
//		mActionBar.hide();
		mActionBar.setTitle("选择群组");// 设置左上角标题
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setHomeButtonEnabled(true);
//		int titleId = getResources().getIdentifier("action_bar_title", "id", "android");
//		TextView abTitle = (TextView) findViewById(titleId);
//		abTitle.setTextColor(getResources().getColor(R.color.actionbar_bkg_black));
		setContentView(R.layout.show_group);
		listView = (ListView) findViewById(R.id.listview_showgroup);
//		rl_actionbar = (RelativeLayout) findViewById(R.id.actionbar_back_rl_showgroup);
//		rl_actionbar.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				finish();
//			}
//		});
		
		groupInfoList = QiXinBaseDao.queryAllGroupInfo();
		
		adapter = new ShowGroupListAdapter(this,groupInfoList);
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				GroupInfo groupInfo = groupInfoList.get(position);
				if("sys".equalsIgnoreCase(groupInfo.groupType)){
					Intent intent = new Intent(ShowGroupActivity.this, NoticeActivity.class);
					Bundle mBundle = new Bundle();
					mBundle.putSerializable(Constant.IM_GOUP_NEWS,
							(Serializable) groupInfo);
					intent.putExtras(mBundle);
					startActivity(intent);
					ShowGroupActivity.this.finish();
				}else{
				Intent intent = new Intent(ShowGroupActivity.this, ChatActivity.class);
				Bundle mBundle = new Bundle();
				mBundle.putSerializable(Constant.IM_GOUP_NEWS,
						(Serializable) groupInfo);
				intent.putExtras(mBundle);
				startActivity(intent);
				ShowGroupActivity.this.finish();
				}
			}
		});
		
	}
}
