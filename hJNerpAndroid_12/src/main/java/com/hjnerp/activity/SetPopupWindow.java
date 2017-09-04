package com.hjnerp.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hjnerp.dao.QiXinBaseDao;
import com.hjnerp.model.UserInfo;
import com.hjnerp.model.WorkflowListInfo;
import com.hjnerp.net.ChatConstants;
import com.hjnerp.net.ChatPacketHelper;
import com.hjnerp.util.ImageLoaderHelper;
import com.hjnerp.util.SharePreferenceUtil;
import com.hjnerp.util.StringUtil;
import com.hjnerpandroid.R;
import com.hjnerpandroid.R.color;

public class SetPopupWindow extends PopupWindow {

	private View mMenuView;
	private ArrayList<WorkflowListInfo> listwork;
	private LinearLayout ll_myinfo,ll_setpwd,ll_about,ll_logout,ll_set;
	private ImageView myphoto;
	protected SharePreferenceUtil sputil;
	private UserInfo myinfo;

	public SetPopupWindow(final Activity context, OnClickListener itemsOnClick) {
		super(context);
		sputil = SharePreferenceUtil.getInstance(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.menu_set, null);
		myinfo = QiXinBaseDao.queryCurrentUserInfo();
		
		ll_myinfo = (LinearLayout) mMenuView.findViewById(R.id.linear_myinfo);
		ll_setpwd = (LinearLayout) mMenuView.findViewById(R.id.linear_setpwd);
		ll_about = (LinearLayout) mMenuView.findViewById(R.id.linear_about);
		ll_logout = (LinearLayout) mMenuView.findViewById(R.id.linear_logout);
		ll_set = (LinearLayout) mMenuView.findViewById(R.id.linear_set);
		
		ll_set.setOnClickListener(itemsOnClick);
		ll_myinfo.setOnClickListener(itemsOnClick);
		ll_setpwd.setOnClickListener(itemsOnClick);
		ll_about.setOnClickListener(itemsOnClick);
		ll_logout.setOnClickListener(itemsOnClick);
		
		TextView tv_name = (TextView) mMenuView.findViewById(R.id.username);
		tv_name.setText(myinfo.username);
		TextView tv_id = (TextView) mMenuView.findViewById(R.id.userid);
		tv_id.setText(myinfo.userID);
		
		myphoto = (ImageView) mMenuView.findViewById(R.id.myphoto);
		String url = myinfo.userImage;
		if(!StringUtil.isNullOrEmpty(url)){
			ImageLoaderHelper.displayImage(ChatPacketHelper.buildImageRequestURL(url, ChatConstants.iq.DATA_VALUE_RES_TYPE_ATTACH),myphoto);}
		int h = context.getWindowManager().getDefaultDisplay().getHeight();
		int w = context.getWindowManager().getDefaultDisplay().getWidth();

		// 设置按钮监听
		// 设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(w/2);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.mystyle);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(color.black);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);

		// // 加载工作流信息
		ListView list = (ListView) mMenuView.findViewById(R.id.listview);

		// listwork = WorkManager.getInstance(context).getWorkListCount();
		// 生成动态数组，加入数据

		// WorkFlowTypeAdapter workflowtypeAdapter = new WorkFlowTypeAdapter(
		// context, listwork);
		// 添加并且显示
		// list.setAdapter(listItemAdapter);
		// 添加点击
		// list.setOnItemClickListener(new OnItemClickListener() {
		// @Override
		// public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		// long arg3) {
		//
		// }
		// });

		// mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
	}

}
