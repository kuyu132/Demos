package com.kuyuzhiqi.testdemo.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.listener.SimpleClickListener
import com.kuyuzhiqi.acrossdemo.ui.FingerPrintActivity
import com.kuyuzhiqi.testdemo.R
import com.tencent.mmkv.MMKV
import io.flutter.embedding.android.FlutterActivity
import org.androidannotations.annotations.EActivity
import java.util.*

@EActivity
class MainActivity : AppCompatActivity() {

    private var tv_hello: TextView? = null
    private var rc_content: RecyclerView? = null
    private var mContentAdapter: ContentAdapter? = null

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
                    add("跳转flutter默认界面")
                    add("在Android界面中加flutter view")
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
                    13 -> startActivity(FlutterActivity.createDefaultIntent(this@MainActivity))
                    14 -> startActivity(Intent(this@MainActivity, FlutterViewActivity::class.java))
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
}
