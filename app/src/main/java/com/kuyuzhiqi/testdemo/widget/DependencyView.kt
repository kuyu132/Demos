package com.kuyuzhiqi.testdemo.widget

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.Button

class DependencyView(context: Context, attrs: AttributeSet?) : Button(context, attrs) {
    var lastX = 0
    var lastY = 0

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.rawX
        val y = event.rawY
        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                val params = layoutParams as CoordinatorLayout.LayoutParams
                val left = params.leftMargin + x - lastX
                val top = params.topMargin + y - lastY
                params.leftMargin = left.toInt()
                params.topMargin = top.toInt()
                layoutParams = params
                requestLayout()
            }
        }
        lastX = x.toInt()
        lastY = y.toInt()
        return true
    }
}