package com.hjnerp.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import com.hjnerp.service.ReConnectReceiver;

public class PollingUtils {

	// 开启轮询服务
	public static void startPollingService(Context context, int seconds) {
		// 获取AlarmManager系统服务
		AlarmManager manager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		// 包装需要执行Service的Intent
		Intent intent = new Intent(context, ReConnectReceiver.class);
		intent.setAction("com.hjnerp.service.websocket.action.reconnect");
		int requestCode = 0;
		PendingIntent pendIntent = PendingIntent.getBroadcast(context,
				requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		// 触发服务的起始时间
		long triggerAtTime = SystemClock.elapsedRealtime();

		// 使用AlarmManger的setRepeating方法设置定期执行的时间间隔（seconds秒）和需要执行的Service
		manager.setRepeating(AlarmManager.ELAPSED_REALTIME, triggerAtTime,
				seconds * 1000, pendIntent);
	}

	// 停止轮询服务
	public static void stopPollingService(Context context) {
		AlarmManager manager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, ReConnectReceiver.class);
		intent.setAction("com.hjnerp.service.websocket.action.reconnect");
		PendingIntent pendingIntent = PendingIntent.getService(context, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		// 取消正在执行的服务
		manager.cancel(pendingIntent);
	}

}
