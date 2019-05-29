package com.kuyuzhiqi.testdemo.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kuyuzhiqi.testdemo.R
import kotlinx.android.synthetic.main.activity_svga.svg_img

class SVGAActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_svga)
        this.svg_img
    }
}
