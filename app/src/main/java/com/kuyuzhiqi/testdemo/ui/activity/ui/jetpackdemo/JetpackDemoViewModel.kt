package com.kuyuzhiqi.testdemo.ui.activity.ui.jetpackdemo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class JetpackDemoViewModel : ViewModel() {
    private val _data = MutableLiveData<String>()
    val data: LiveData<String>
        get() = _data

    init {
        _data.value = "hello, jetpack"
    }
}
