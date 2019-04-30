package com.kuyuzhiqi.testdemo.service

import android.app.ActivityManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.*
import android.util.Log
import com.kuyuzhiqi.testdemo.MSG_FROM_CLIENT
import com.kuyuzhiqi.testdemo.MSG_FROM_SERVER

class MessengerService : Service() {

    open val TAG = "MessengerSerive"
    val mMessenger: Messenger = Messenger(MessengerHandler())

    private inner class MessengerHandler : Handler() {

        override fun handleMessage(msg: Message) {
            Log.i(TAG, Thread.currentThread().name + "|" + getProcessName(android.os.Process.myPid()))
            val data = msg.data
            when (msg.what) {
                MSG_FROM_CLIENT -> {
                    val notice = data.getString("msg")
                    Log.i(TAG, "receive from Client: $notice")
                    myAsyncTask.execute()
                    val client: Messenger = msg.replyTo
                    val replyMessage = Message.obtain(null, MSG_FROM_SERVER)
                    val bundle = Bundle()
                    bundle.putString("reply", "Server has already received your msg")
                    replyMessage.data = bundle
                    try {
                        client.send(replyMessage)
                    } catch (e: RemoteException) {
                        e.printStackTrace()
                    }
                }
                /**
                 * server内部的信息传递
                 */
                MSG_FROM_SERVER -> {
                    val sum = data.getInt("sum")
                    if (sum > 0) {
                        Log.i(TAG, "" + sum)
                    }
                }
                else -> {
                    super.handleMessage(msg)
                }
            }
        }
    }

    private val myAsyncTask = object : AsyncTask<String, Integer, String>() {
        var sum = 0

        override fun doInBackground(vararg params: String?): String {
            Log.i(TAG,
                    "doInBackground" + Thread.currentThread().name + "|" + getProcessName(android.os.Process.myPid()))
            for (i in 0..100) {
                sum += i
                Thread.sleep(10)
            }
            return ""
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            val replyMessage = Message.obtain(null, MSG_FROM_SERVER)
            val bundle = Bundle()
            replyMessage.data = bundle
            Log.i(TAG, "async task has done")
            bundle.putInt("sum", sum)
            Log.i(TAG, Thread.currentThread().name + "|" + getProcessName(android.os.Process.myPid()))
            try {
                mMessenger.send(replyMessage)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

    override fun onBind(intent: Intent?): IBinder {
        return mMessenger.binder
    }

    fun getProcessName(pid: Int): String {
        val mActivityManager = this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (appProcess in mActivityManager
                .runningAppProcesses) {
            if (appProcess.pid == pid) {
                return appProcess.processName
            }
        }
        return ""
    }

}