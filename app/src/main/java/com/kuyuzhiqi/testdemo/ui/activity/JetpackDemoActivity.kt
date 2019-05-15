package com.kuyuzhiqi.testdemo.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kuyuzhiqi.testdemo.R
import com.kuyuzhiqi.testdemo.ui.activity.ui.jetpackdemo.JetpackDemoFragment

class JetpackDemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.jetpack_demo_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, JetpackDemoFragment.newInstance())
                    .commitNow()
        }
    }

}
