package com.mznerp.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.widget.Toast;

import com.mznerp.common.EapApplication;

/**
 * 
 * 重连接服务.
 * 
 * @author 李庆义
 */
public class ReConnectService extends Service {

	private Context context;
	private ConnectivityManager connectivityManager;
	private NetworkInfo info;

	public static final String ACTION = "com.ryantang.service.PollingService";

	@Override
	public void onCreate() {
		context = this;
		IntentFilter mFilter = new IntentFilter();
		mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);// 网络变化事件
		mFilter.addAction(Intent.ACTION_SCREEN_ON);// 屏幕解锁事件
		mFilter.addAction(ACTION);// 屏幕解锁事件
		registerReceiver(reConnectionBroadcastReceiver, mFilter);

		super.onCreate();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		new PollingThread().start();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		unregisterReceiver(reConnectionBroadcastReceiver);
		super.onDestroy();
	}

	BroadcastReceiver reConnectionBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)
					|| action.equalsIgnoreCase(Intent.ACTION_SCREEN_ON)) {

				connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
				info = connectivityManager.getActiveNetworkInfo();

				if (info != null && info.isAvailable()) {
					reConnect();
				} else {
				//	sendInentAndPre(Constant.RECONNECT_STATE_FAIL);
					Toast.makeText(context, "网络断开,用户已离线!", Toast.LENGTH_SHORT)
							.show();
				}
			}

		}

	};

	/**
	 * 
	 * 递归重连，直连上为止.
	 * 
	 * @author 李庆义
	 * @update 2012-7-10 下午2:12:25
	 */
	public void reConnect() {
		//
		String URL = EapApplication.getApplication().URL_SERVER_HOST_HTTP;
		if (URL == null || "".equalsIgnoreCase(URL)) {
			return;
		}
		// /查看是否已登录 如果已登录
		// EapApplication.getApplication().putExtra(
		// EapApplication.EXTRA_AUTO_LOGIN, ui.isAutoLogin);
		try {

			// HJWebSocketClient hjWebSockectClient =
			// HJWebSocketManager.getInstance().getConnection();
			// if (hjWebSockectClient == null)
			// {
			// HJWebSocketManager.getInstance().connect(URL);
			// }
			// else
			// {
			// if (HJWebSocketManager.getInstance().getConnection().isOpen())
			// return ;
			// if
			// (HJWebSocketManager.getInstance().getConnection().isConnecting())
			// return ;//webxocket连接正常
			// HJWebSocketManager.getInstance().connect(URL);
			// }
			//
			// String errorText = HJWebSocketManager.getInstance().autoLogin();
			//
			// if ("".equalsIgnoreCase(errorText))
			// {
			//
			// Intent chatServer = new Intent(context, IMChatService.class);
			// context.startService(chatServer);
			//
			// Intent imSystemMsgService = new
			// Intent(context,IMIqService.class);
			// context.startService(imSystemMsgService);
			//
			// HJWebSocketManager.getInstance().readyPresence();
			// Toast.makeText(context, "网络连接成功,用户已上线!",
			// Toast.LENGTH_SHORT).show();
			// }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			reConnect();
			return;
		}

	}



	/**
	 * Polling thread 模拟向Server轮询的异步线程
	 * 
	 * @Author Ryan
	 * @Create 2013-7-13 上午10:18:34
	 */
	int count = 0;

	class PollingThread extends Thread {
		@Override
		public void run() {
			System.out.println("Polling...");
			count++;
			// 当计数能被5整除时弹出通知
			if (count % 5 == 0) {
				// showNotification();
				System.out.println("New message!");
			}
		}
	}

}
