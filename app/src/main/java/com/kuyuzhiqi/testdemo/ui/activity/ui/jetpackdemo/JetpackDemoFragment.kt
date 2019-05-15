package com.kuyuzhiqi.testdemo.ui.activity.ui.jetpackdemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.kuyuzhiqi.testdemo.R

class JetpackDemoFragment : Fragment() {

    companion object {
    fun newInstance() = JetpackDemoFragment()
}

    private lateinit var viewModel: JetpackDemoViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.jetpack_demo_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(JetpackDemoViewModel::class.java)
    }

}
