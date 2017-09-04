package com.hjnerp.activity.contact;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import com.hjnerp.util.SharePreferenceUtil;
import com.hjnerpandroid.R;
import com.hjnerpandroid.R.color;

public class FriendPopupWindow extends PopupWindow {

	private View mMenuView;
	private LinearLayout ll_delete;
	protected SharePreferenceUtil sputil;

	public FriendPopupWindow(final Activity context, OnClickListener itemsOnClick) {
		super(context);
		sputil = SharePreferenceUtil.getInstance(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.friend_pop, null);
		
		ll_delete = (LinearLayout) mMenuView.findViewById(R.id.linear_delete);
		ll_delete.setOnClickListener(itemsOnClick);

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


	}

}
