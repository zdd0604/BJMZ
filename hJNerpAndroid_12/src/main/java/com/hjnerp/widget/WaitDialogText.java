package com.hjnerp.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hjnerpandroid.R;

public class WaitDialogText extends Dialog {
	private TextView tv;

	public WaitDialogText(Context context) {
		super(context, R.style.noticeDialogStyle);
	}

	private WaitDialogText(Context context, int theme) {
		super(context, theme);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_wait_withtext2);
//		Log.e("WaitDialog","oncreat");
		tv = (TextView)this.findViewById(R.id.tv_waitdialog2_notice);
		LinearLayout linearLayout = (LinearLayout)this.findViewById(R.id.ly_LinearLayoutText);
		linearLayout.getBackground().setAlpha(100);
//		tv.setText("正在加载。。。");
		
		setCanceledOnTouchOutside(false); 
		//setCancelable(false);   //backbtn of phoneself
	}
	
	public void setTextView(String msg){
		this.tv.setText(msg);
	}
	
}