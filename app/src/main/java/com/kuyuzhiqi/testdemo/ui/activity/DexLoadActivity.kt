package com.kuyuzhiqi.testdemo.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import com.kuyuzhiqi.testdemo.R
import dalvik.system.DexClassLoader
import kotlinx.android.synthetic.main.activity_dex_load.btn_dex
import java.io.File
import java.lang.Exception

class DexLoadActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dex_load)
        btn_dex.setOnClickListener {
            val dexPath = Environment.getExternalStorageDirectory().absolutePath + File.separator + "classes.dex"
            Toast.makeText(this@DexLoadActivity, dexPath, Toast.LENGTH_SHORT).show()
            val loader = DexClassLoader(dexPath, null, null, classLoader)
            try {
                loader.parent
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
