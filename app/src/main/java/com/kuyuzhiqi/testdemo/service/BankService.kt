package com.kuyuzhiqi.testdemo.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.kuyuzhiqi.testdemo.binder.BankBinder

class BankService: Service() {
    override fun onBind(intent: Intent?): IBinder {
        return BankBinder()
    }
}