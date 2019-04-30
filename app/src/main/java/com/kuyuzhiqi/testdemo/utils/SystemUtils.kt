package com.kuyuzhiqi.testdemo.utils

import android.app.ActivityManager
import android.content.Context

class  SystemUtils {
    companion object{
        fun getProcessName(context: Context, pid: Int): String {
            val mActivityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            for (appProcess in mActivityManager
                    .runningAppProcesses) {
                if (appProcess.pid == pid) {
                    return appProcess.processName
                }
            }
            return ""
        }
    }

}
