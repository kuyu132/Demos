package com.kuyuzhiqi.testdemo.ui.activity

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.kuyuzhiqi.testdemo.R
import com.kuyuzhiqi.testdemo.Status
import kotlinx.android.synthetic.main.activity_path_animation.status_view
import kotlinx.android.synthetic.main.activity_path_animation.switch_view

class PathAnimationActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_path_animation)

        initViews()
    }

    private fun initViews() {
        switch_view.setOnClickListener {
            val status = switch_view.getStatus()
            when (status) {
                Status.CONNECTING -> {
                    Handler().postDelayed({
                        switch_view.setStatus(Status.CONNECTED)
                        status_view.setStatus(Status.CONNECTED)
                    }, 3000)
                }
                Status.CONNECTED -> {
                    switch_view.setStatus(Status.DISCONNECTING)
                    status_view.setStatus(Status.DISCONNECTING)
                    Handler().postDelayed({
                        switch_view.setStatus(Status.NOT_CONNECTED)
                        status_view.setStatus(Status.NOT_CONNECTED)
                    }, 3000)
                }
                Status.NOT_CONNECTED, Status.DISCONNECTING -> {
                    status_view.setStatus(Status.CONNECTING)
                    switch_view.setStatus(Status.CONNECTING)
                    Handler().postDelayed({
                        switch_view.setStatus(Status.CONNECTED)
                        status_view.setStatus(Status.CONNECTED)
                    }, 3000)
                }
            }

        }
    }
}