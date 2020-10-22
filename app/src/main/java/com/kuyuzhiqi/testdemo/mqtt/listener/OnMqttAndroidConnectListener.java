package com.kuyuzhiqi.testdemo.mqtt.listener;

public abstract class OnMqttAndroidConnectListener {
    public void connect() {

    }

    public void disConnect() {

    }

    public abstract void onDataReceive(String message);

    public void onConnectFail(String exception) {

    }
}
