package com.kuyuzhiqi.testdemo.mqtt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.kuyuzhiqi.testdemo.R
import com.kuyuzhiqi.testdemo.mqtt.listener.OnMqttAndroidConnectListener
import kotlinx.android.synthetic.main.activity_mqttest.*

class MQTTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mqttest)

        MqttManager.getInstance()
                .init(application)
                .setServerIp(Api.IP_MQTT)                            //Ip
                .setServerPort(Api.PORT_MQTT)                        //port
                .connect()

        MqttManager.getInstance().regeisterServerMsg(object :OnMqttAndroidConnectListener(){
            override fun onDataReceive(message: String?) {
                Log.e("收到消息了,内容是 ", message);
                tv_msg.text = message
            }
        })
        tv_send.setOnClickListener {
            Log.e("点击了", "点击了")
            MqttManager.getInstance().sendMsg(Api.TOPIC, "你好吗?我发一条消息试试")
        }
    }
}
