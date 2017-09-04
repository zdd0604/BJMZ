package com.hjnerp.business.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hjnerp.business.BusinessLua;
import com.hjnerp.common.ActivitySupport;
import com.hjnerp.model.Ctlm1347;
import com.hjnerp.widget.HJPhotoView;
import com.hjnerpandroid.R;


public class SearchBussinessPicture extends ActivitySupport {
	
	public static final String TAG = SearchBussinessPicture.class.getSimpleName();
	
	private String billNo;
	private String parentNode;
	private RelativeLayout actionBar;
	private TextView title;
	private LinearLayout ll_view; 
	private List<HJPhotoView> hjPhotoViewList = new ArrayList<HJPhotoView>();
	List<Ctlm1347> list;
	HJPhotoView mHjPhotoView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_bussiness_picture);
		mActionBar.hide();
		actionBar = (RelativeLayout) findViewById(R.id.rl_actionbar_back);
		ll_view = (LinearLayout) findViewById(R.id.ll_view);
		title = (TextView) findViewById(R.id.tv_actionbar_title);
		title.setText("照片浏览");
		actionBar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		Intent intent = getIntent();
		billNo = intent.getStringExtra("billNo");
		parentNode = intent.getStringExtra("idnode");
		
		
		//开始显示照片的相关信息
		list = BusinessLua.searchPictureByParent(billNo, parentNode);
		
		ll_view.removeAllViews();
		for(int i = 0;i < list.size();i++){
			StringBuffer sb = new StringBuffer();
			sb.append(list.get(i).getVar_data1());
			sb.append(";");
			if(sb.length() > 0){
				sb.deleteCharAt(sb.length()-1);
			}
			List<String>arrays = new ArrayList<String>(Arrays.asList(sb.toString().split(";"))) ;
			HJPhotoView view = new HJPhotoView(context,list.get(i));
			hjPhotoViewList.add(view);
			
			ll_view.addView(view);
			view.setAdapterArray(arrays);
			
		}
		
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();

		
//		enterHjPhotoView.setAdapterArray(arrays);
		//添加离店照片
		//添加堆头照片
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode==RESULT_OK&&requestCode==1001) {//HJPhotoView控件拍照返回
				for(int i =0;i<list.size();i++){
					if(hjPhotoViewList.get(i).istakingphoto){
						mHjPhotoView = hjPhotoViewList.get(i);
					}
					
					
				}		
			
			new Handler().postDelayed(new Runnable() { 
							@Override
							public void run() {
								
//								mHjPhotoView.arrayStr.add(mHjPhotoView.strImage);
								mHjPhotoView.refreshGallery();
								mHjPhotoView.saveCTLM1347();
								mHjPhotoView.istakingphoto = false;
								
							}
						}, 1000);//部分低端手机（三星tab手机测试）写文件速度较慢，需要此延时
	}
	

}
}
