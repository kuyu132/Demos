package com.kuyuzhiqi.testdemo.mqtt.bean;

import com.kuyuzhiqi.testdemo.mqtt.listener.OnMqttAndroidConnectListener;

public class MqttListenerData {
    private String event;
    private OnMqttAndroidConnectListener listener;

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public OnMqttAndroidConnectListener getListener() {
        return listener;
    }

    public void setListener(OnMqttAndroidConnectListener listener) {
        this.listener = listener;
    }
}
