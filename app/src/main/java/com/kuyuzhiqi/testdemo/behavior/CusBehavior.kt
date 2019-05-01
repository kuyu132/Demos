package com.kuyuzhiqi.testdemo.behavior

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.kuyuzhiqi.testdemo.R

class CusBehavior<V:View>(context: Context, attr: AttributeSet) : CoordinatorLayout.Behavior<V>(context, attr) {

    private var width = 0

    init {
        val dm = context.resources.displayMetrics
        width = dm.widthPixels
    }

//    override fun layoutDependsOn(parent: CoordinatorLayout?, child: V, dependency: View?): Boolean {
//        return dependency!!.id == R.id.dependency
//    }
//
//    override fun onDependentViewChanged(parent: CoordinatorLayout?, child: V, dependency: View?): Boolean {
//        child!!.y  = dependency!!.y + child!!.height
//        child!!.x = dependency!!.x
//        return true
//    }

}