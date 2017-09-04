package com.hjnerp.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
	public static void ShowLong(Context mContext, String msg) {
		Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
	}

	public static void ShowShort(Context mContext, String msg) {

		Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
	}
}
