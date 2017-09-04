package com.hjnerp.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.hjnerp.activity.MainActivity;

import java.util.Timer;
import java.util.TimerTask;

public class WorkService extends Service {
    private Timer timer;
    private TimerTask task;
    private int count;

    public WorkService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.d("service", "onBind");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        final Intent intent = new Intent();
        intent.setAction("com.hjnerp.service.Work");
//        timer = new Timer();
//        task = new TimerTask() {
//            //
//            @Override
//            public void run() {
        sendBroadcast(intent);
        Log.d("service", "onCreate");
//            }
//        };
//        timer.schedule(task, 0, 10000);
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
//        final Intent intent1 = new Intent();
//        intent.setAction("com.hjnerp.service.Work");
//        timer = new Timer();
//        task = new TimerTask() {
//
//            @Override
//            public void run() {
//                sendBroadcast(intent1);
//                Log.d("service","onCreate");
//            }
//        };
//        timer.schedule(task, 1000, 30000);
        Log.d("service", "onStartCommand");
        timer = new Timer();
        task = new TimerTask() {

            @Override
            public void run() {
//                sendBroadcast(intent);
                MainActivity.newMain.readWorkMenu();
            }
        };
        timer.schedule(task, 1000, 30000);

        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
        Log.d("service", "onDestroy");
    }

}
