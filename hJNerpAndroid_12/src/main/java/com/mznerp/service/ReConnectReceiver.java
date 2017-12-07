package com.mznerp.service;

import java.util.List;

import com.mznerp.websocket.HjHTTPNotificationManager;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ReConnectReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
//		 Toast.makeText(context, "收到广播了", Toast.LENGTH_SHORT).show();

		String action = intent.getAction();
		ConnectivityManager connectivityManager;
		NetworkInfo info;
		
		  if (action.equals("com.hjnerpandroid.service.websocket.action.reconnect")) {

			connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			info = connectivityManager.getActiveNetworkInfo();

			if (info != null && info.isAvailable()) {
				if (HjHTTPNotificationManager.getInstance().isStoped() && isServiceRunning(context,"com.hjnerpandroid.service.IMChatService") )
				{
					//重启聊天服务
					Intent chatServer = new Intent(context, IMChatService.class);
					context.startService(chatServer);
				}
			}  
		} 
		 
	}


/**

        * 用来判断服务是否运行.

        * @param context

        * @param className 判断的服务名字

        * @return true 在运行 false 不在运行

        */

       public static boolean isServiceRunning(Context mContext,String className) {

           boolean isRunning = false;

          ActivityManager activityManager = (ActivityManager) 
        		  mContext.getSystemService(Context.ACTIVITY_SERVICE); 

           List<ActivityManager.RunningServiceInfo> serviceList    = activityManager.getRunningServices(30);

          if (!(serviceList.size()>0)) { 
               return false; 
           }

           for (int i=0; i<serviceList.size(); i++) { 
               if (serviceList.get(i).service.getClassName().equals(className) == true) { 
                   isRunning = true; 
                   break; 
               } 
           }

           return isRunning;

       }
}
