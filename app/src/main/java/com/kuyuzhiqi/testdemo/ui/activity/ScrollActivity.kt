package com.kuyuzhiqi.testdemo.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.kuyuzhiqi.testdemo.R
import com.kuyuzhiqi.testdemo.utils.DisplayUtils
import kotlinx.android.synthetic.main.activity_scroll.rc
import kotlinx.android.synthetic.main.activity_scroll.sll
import kotlinx.android.synthetic.main.activity_scroll.v_blank

class ScrollActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scroll)
        val list = ArrayList<String>().apply {
            add("dadas")
            add("dadas")
            add("dadas")
            add("dadas")
            add("dadas")
            add("dadas")
            add("dadas")
            add("dadas")
            add("dadas")
            add("dadas")
            add("dadas")
            add("dadas")
            add("dadas")
            add("dadas")
            add("dadas")
            add("dadas")
            add("dadas")
            add("dadas")
            add("dadas")
            add("dadas")}
        rc.adapter = DemoAdapter(list)
        rc.layoutManager = LinearLayoutManager(this)
        rc.addOnItemTouchListener(object :OnItemClickListener(){
            override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                Toast.makeText(this@ScrollActivity, "toashsada", Toast.LENGTH_SHORT).show()
            }
        })
        val height = 320f
        val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                DisplayUtils.dipToPx(this,height))
        v_blank.layoutParams = layoutParams
        v_blank.setOnClickListener {
            Toast.makeText(this@ScrollActivity, "v_blank", Toast.LENGTH_SHORT).show()
        }
        sll.setBlankViewHeight(DisplayUtils.dipToPx(this,height))
    }

    class  DemoAdapter(list:List<String>):BaseQuickAdapter<String,BaseViewHolder>(R.layout.item_demo, list) {
        override fun convert(helper: BaseViewHolder, item: String) {
            helper.setText(R.id.tv_name, item)
        }

    }
}
