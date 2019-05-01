package com.kuyuzhiqi.testdemo.ui.activity

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kuyuzhiqi.testdemo.IBankAIDL
import com.kuyuzhiqi.testdemo.R
import com.kuyuzhiqi.testdemo.service.BankService

class AIDLTestActivity : AppCompatActivity() {

    private var bankBinder: IBankAIDL? = null

    private val conn = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            bankBinder = IBankAIDL.Stub.asInterface(service)
        }

        override fun onServiceDisconnected(name: ComponentName) {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aidltest)
        initView()
        initData()
    }

    private fun initView() {
        findViewById<Button>(R.id.btn_open_account).setOnClickListener {
            val str = bankBinder!!.openAccount("kuyu", "123456")
            Toast.makeText(this@AIDLTestActivity, str, Toast.LENGTH_LONG).show()
        }
    }

    private fun initData() {
        val intent = Intent(this, BankService::class.java)
        bindService(intent, conn, Context.BIND_AUTO_CREATE)
    }
}
