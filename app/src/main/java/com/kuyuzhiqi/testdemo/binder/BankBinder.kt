package com.kuyuzhiqi.testdemo.binder

import com.kuyuzhiqi.testdemo.IBankAIDL

class BankBinder : IBankAIDL.Stub() {
    override fun openAccount(name: String, password: String): String {
        return name + "开户成功"
    }

}