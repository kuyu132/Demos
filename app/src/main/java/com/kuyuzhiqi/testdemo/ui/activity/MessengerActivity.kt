package com.kuyuzhiqi.testdemo.ui.activity

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.kuyuzhiqi.testdemo.MSG_FROM_CLIENT
import com.kuyuzhiqi.testdemo.MSG_FROM_SERVER
import com.kuyuzhiqi.testdemo.R
import com.kuyuzhiqi.testdemo.service.MessengerService
import com.kuyuzhiqi.testdemo.utils.SystemUtils

class MessengerActivity : AppCompatActivity() {

    private lateinit var mService: Messenger
    open val TAG = "MessengerSerive"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messenger)
        initData()
    }

    private fun initData() {
        val intent = Intent(this@MessengerActivity, MessengerService::class.java)
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
    }

    private val mGetReplyMessenger: Messenger = Messenger(MessengerHandler())

    private val mConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            Log.i(TAG,
                    "ServiceConnection: " + Thread.currentThread().name + "|" + SystemUtils.getProcessName(
                            this@MessengerActivity, android.os.Process.myPid()))

            mService = Messenger(service)
            val msg = Message.obtain(null, MSG_FROM_CLIENT)
            val data = Bundle()
            data.putString("msg", "hello,this is client calling.")
            msg.data = data
            msg.replyTo = mGetReplyMessenger
            try {
                mService.send(msg)
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
        }

        override fun onServiceDisconnected(name: ComponentName) {

        }
    }

    private inner class MessengerHandler : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MSG_FROM_SERVER -> {
                    val data = msg.data
                    val notice = data.getString("reply")
                    if (!TextUtils.isEmpty(notice)) {
                        Toast.makeText(this@MessengerActivity, notice, Toast.LENGTH_SHORT).show()
                        Log.i(TAG, notice)
                    }

                    val sum = data.getInt("sum")
                    if (sum > 0) {
                        Toast.makeText(this@MessengerActivity, "" + sum, Toast.LENGTH_SHORT).show()
                        Log.i(TAG, "" + sum)
                    }

                }
                else -> {
                    super.handleMessage(msg)
                }
            }
        }
    }

}
