package com.kuyuzhiqi.testdemo.sensor;

import android.content.Context;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class Sensor {


    public Sensor(Context context) {
        //测试距离感应器
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        android.hardware.Sensor mSensor = sensorManager.getDefaultSensor(android.hardware.Sensor.TYPE_PROXIMITY);
        sensorManager.registerListener(sensorEventListener, mSensor, 10);
    }

    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == android.hardware.Sensor.TYPE_PROXIMITY) {
                Log.i("tencent", "距离传感器数据：" + event.values[0]);
            }
        }

        @Override
        public void onAccuracyChanged(android.hardware.Sensor sensor, int accuracy) {

        }
    };
}
