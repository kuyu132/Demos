package com.kuyuzhiqi.testdemo.ui.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.os.PowerManager
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.listener.SimpleClickListener
import com.kuyuzhiqi.acrossdemo.ui.FingerPrintActivity
import com.kuyuzhiqi.testdemo.R
import com.kuyuzhiqi.testdemo.mqtt.MQTTestActivity
import com.kuyuzhiqi.testdemo.utils.LooperPrinter
import com.kuyuzhiqi.testdemo.utils.ShellUtils
import com.kuyuzhiqi.testdemo.utils.ThreadUtils
import com.tencent.mmkv.MMKV
import org.androidannotations.annotations.EActivity
import java.util.ArrayList
import java.util.Timer
import java.util.TimerTask

@EActivity
class MainActivity : AppCompatActivity() {

    private var tv_hello: TextView? = null
    private var rc_content: RecyclerView? = null
    private var mContentAdapter: ContentAdapter? = null
    val REQUEST_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val dir = MMKV.initialize(this)
        Toast.makeText(this, dir, Toast.LENGTH_LONG).show()

        tv_hello = findViewById(R.id.tv_hello)
        rc_content = findViewById(R.id.rc_content)
        val contentList = ArrayList<String>()
                .apply {
                    add("Xposed测试")
                    add("登录Xposed劫持")
                    add("MMKV测试")
                    add("吸顶滑动列表")
                    add("吸顶滑动tab")
                    add("指纹识别")
                    add("Path Animation")
                    add("Messenger")
                    add("json读取")
                    add("SVGA动画")
                    add("旋转控件")
                    add("Gif")
                    add("lottie animation")
                    add("加载dex文件")
                    add("mqtt")
                }

        mContentAdapter = ContentAdapter(contentList)
        rc_content!!.adapter = mContentAdapter
        rc_content!!.layoutManager = LinearLayoutManager(this)
        rc_content!!.addOnItemTouchListener(object : SimpleClickListener() {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                when (position) {
                    0 -> tv_hello!!.text = "天平"
                    1 -> startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                    2 -> startActivity(Intent(this@MainActivity, MMKVActivity::class.java))
                    3 -> startActivity(Intent(this@MainActivity, ScrollActivity::class.java))
                    4 -> startActivity(Intent(this@MainActivity, ScrollTabActivity2::class.java))
                    5 -> startActivity(Intent(this@MainActivity, FingerPrintActivity::class.java))
                    6 -> startActivity(Intent(this@MainActivity, PathAnimationActivity::class.java))
                    7 -> startActivity(Intent(this@MainActivity, MessengerActivity::class.java))
                    8 -> startActivity(Intent(this@MainActivity, JsonActivity::class.java))
                    9 -> startActivity(Intent(this@MainActivity, SVGAActivity::class.java))
                    10 -> startActivity(Intent(this@MainActivity, CarrouselActivity::class.java))
                    11 -> startActivity(Intent(this@MainActivity, GifActivity::class.java))
                    12 -> startActivity(Intent(this@MainActivity, LottieActivity::class.java))
                    13 -> startActivity(Intent(this@MainActivity, DexLoadActivity::class.java))
                    14 -> startActivity(Intent(this@MainActivity, MQTTestActivity::class.java))
                }
            }

            override fun onItemLongClick(adapter: BaseQuickAdapter<*, *>, view: View,
                                         position: Int) {
            }

            override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View,
                                          position: Int) {
            }

            override fun onItemChildLongClick(adapter: BaseQuickAdapter<*, *>, view: View,
                                              position: Int) {
            }
        })
        object : Thread() {
            override fun run() {
                tv_hello!!.text = "线程改变tv值"
            }
        }.start()

        //判断是否在doze白名单中，如果不是的话，则申请
        //1.manifest配置；2.权限动态申请
        val pm = getSystemService(Context.POWER_SERVICE) as PowerManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val result = pm.isIgnoringBatteryOptimizations(this.packageName)
            Toast.makeText(this, "doze白名单:" + result, Toast.LENGTH_SHORT).show()
            if (!result) {
                val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
                intent.data = Uri.parse("package:$packageName")
                startActivity(intent)
            }
        }

        Looper.getMainLooper().setMessageLogging(LooperPrinter())

        val stackTraces = Looper.getMainLooper().thread.stackTrace
//        for (stack in stackTraces){
//            Log.i("kuyu","" + stack.lineNumber+" || "+stack.className +" || "+stack.methodName )
//        }

        val isRoot = ShellUtils.checkRootPermission()
        val threadUtils = ThreadUtils()
        threadUtils.printInfo(isRoot)
        val task = object : TimerTask() {
            override fun run() {
                Log.i("kuyu", "延时任务")
                threadUtils.printInfo(isRoot)
            }
        }

        val timer = Timer()
        timer.schedule(task, 300)

    }

    internal inner class ContentAdapter(data: List<String>) :
            BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_content, data) {

        override fun convert(helper: BaseViewHolder, item: String) {
            helper.setText(R.id.tv_title, item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE) {
            for (i in grantResults) {
//                if(i == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this, "" + i, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
