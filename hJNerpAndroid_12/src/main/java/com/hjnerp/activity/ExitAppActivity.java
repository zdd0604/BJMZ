package com.hjnerp.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class ExitAppActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		exitAPP();
	}
	
	//3s后重启 app
	public void exitAPP(){
		Intent intent = new Intent(this, LoginActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent restartIntent = PendingIntent.getActivity(    
                 getApplicationContext(), 0, intent,    
                 Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);   
		 AlarmManager mgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);    
         mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 3000,    
                 restartIntent);
//         Process.killProcess(Process.myPid());   
         System.exit(0);
	}
}
