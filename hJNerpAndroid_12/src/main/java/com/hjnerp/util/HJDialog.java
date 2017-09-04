package com.hjnerp.util;

import com.hjnerpandroid.R;
import android.app.Activity;
import android.app.Dialog;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

public class HJDialog extends Dialog {
	int dialogResult;
	Handler mHandler;
	String titleName;

	public HJDialog(Activity context, String mailName, boolean retry) {
		super(context, R.style.noticeDialogStyle);
		setOwnerActivity(context);
		onCreate();
		this.titleName = mailName;
	}

	public int getDialogResult() {
		return dialogResult;
	}

	public void setDialogResult(int dialogResult) {
		this.dialogResult = dialogResult;
	}

	/** Called when the activity is first created. */

	public void onCreate() {
		setContentView(R.layout.dialog_notice_withcancel);
		((TextView) findViewById(R.id.dialog_cancel_tv)).setText("拍照");
		((TextView) findViewById(R.id.dialog_confirm_tv)).setText("定位");
		((TextView) findViewById(R.id.dialog_notice_tv))
				.setText("定位不成功，是否继续定位？");
		findViewById(R.id.dialog_cancel_tv).setOnClickListener(
				new android.view.View.OnClickListener() {
					@Override
					public void onClick(View paramView) {
						endDialog(0);
					}
				});

		findViewById(R.id.dialog_confirm_tv).setOnClickListener(
				new android.view.View.OnClickListener() {
					@Override
					public void onClick(View paramView) {
						endDialog(1);
					}

				});
	}

	public void endDialog(int result)

	{
		dismiss();
		setDialogResult(result);
		Message m = mHandler.obtainMessage();
		mHandler.sendMessage(m);
	}

	public int showDialog() {

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message mesg) {

				// process incoming messages here

				// super.handleMessage(msg);
				throw new RuntimeException();
			}

		};

		super.show();
		try {
			Looper.getMainLooper().loop();
		}

		catch (RuntimeException e2) {
		}

		return dialogResult;

	}
}
