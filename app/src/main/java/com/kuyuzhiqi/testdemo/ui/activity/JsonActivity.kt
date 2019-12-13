package com.kuyuzhiqi.testdemo.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.gson.reflect.TypeToken
import com.kuyuzhiqi.testdemo.R
import com.kuyuzhiqi.testdemo.databinding.ActivityJsonBinding
import com.kuyuzhiqi.testdemo.model.User
import com.kuyuzhiqi.testdemo.utils.GsonUtils

class JsonActivity : AppCompatActivity() {

    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityJsonBinding>(this, R.layout.activity_json)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        iniData()
    }

    private fun iniData() {
        val result = GsonUtils.loadJsonFromLocal(this, "mock_data.json")
        val userList = GsonUtils.gson.fromJson<List<User>>(result, object : TypeToken<List<User>>() {}.type)
    }
}
