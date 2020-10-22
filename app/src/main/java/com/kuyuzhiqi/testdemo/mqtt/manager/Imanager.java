package com.kuyuzhiqi.testdemo.mqtt.manager;

import com.kuyuzhiqi.testdemo.mqtt.listener.OnMqttAndroidConnectListener;

public interface Imanager {

    void connect();

    void disConnect();

    void regeisterServerMsg(OnMqttAndroidConnectListener listener);

    void unRegeisterServerMsg(OnMqttAndroidConnectListener listener);

    void sendMsg(String topic, String message);

    boolean isConnected();
}
