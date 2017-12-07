package com.mznerp.business.businessutils;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by zdd on 2017/2/21.
 */

public class MyOrientationListener implements SensorEventListener {
    private SensorManager sensorManager;
    private Context context;
    private Sensor sensor;
    private float lastX;

    public OnOrientationListener onOrientationListener;

    public void setOnOrientationListener(OnOrientationListener onOrientationListener) {
        this.onOrientationListener = onOrientationListener;
    }

    public void start() {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            //获得方向传感器
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        }

        if (sensor != null) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    public void stop() {
        sensorManager.unregisterListener(this);
    }

    public MyOrientationListener(Context context) {
        this.context = context;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // 接受方向感应器的类型
        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION)
        {
            // 这里我们可以得到数据，然后根据需要来处理
            float x = event.values[SensorManager.DATA_X];

            if( Math.abs(x- lastX) > 1.0 )
            {
                onOrientationListener.onOrientationChanged(x);
            }
//            Log.e("DATA_X", x+"");
            lastX = x ;

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public interface OnOrientationListener {
        void onOrientationChanged(float x);
    }
}
