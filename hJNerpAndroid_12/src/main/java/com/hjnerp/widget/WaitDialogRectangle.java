package com.hjnerp.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hjnerpandroid.R;

public class WaitDialogRectangle extends Dialog {
	private TextView tv;
	private Context context;
	private View customView;

	public WaitDialogRectangle(Context context) {	
		super(context, R.style.noticeDialogStyle);
		this.context = context;
		
	}

	private WaitDialogRectangle(Context context, int theme) {
		super(context, theme);
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater= LayoutInflater.from(context);
        customView = inflater.inflate(R.layout.dialog_wait_withtext, null);
		
		setContentView(customView); 
		tv = (TextView)this.findViewById(R.id.tv_waitdialog);
		LinearLayout linearLayout = (LinearLayout)this.findViewById(R.id.ly_waitdialog);
		linearLayout.getBackground().setAlpha(180);
		tv.setText("正在加载数据");
		
		setCanceledOnTouchOutside(false); 
		//setCancelable(false);   //backbtn of phoneself
	}
	@Override
	public View findViewById(int id) {
	// TODO Auto-generated method stub
	return super.findViewById(id);
	}
	public View getCustuoView(){
		return customView;
	}
	
	public void setText(String text)
	{
		this.tv.setText(text);
	}
	
}
