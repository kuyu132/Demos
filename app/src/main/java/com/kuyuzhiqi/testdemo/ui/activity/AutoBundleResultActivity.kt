package com.kuyuzhiqi.testdemo.ui.activity

import android.os.Bundle
import android.os.Parcelable
import android.util.SparseArray
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import autobundle.AutoBundle
import autobundle.annotation.Required
import autobundle.annotation.Unbox
import com.kuyuzhiqi.testdemo.R

class AutoBundleResultActivity : AppCompatActivity() {

    @Required
    @Unbox("loginName")
    var loginName: String? = null
    @Required
    @Unbox("password")
    var password: String? = null

    @Unbox("int")
    var intValue: Int = 0

    @Unbox("string")
    var stringValue: String? = null

    @Unbox("intArray")
    var intArrayValue: IntArray? = null

    @Unbox("stringArray")
    var stringArrayValue: Array<String>? = null

    @Unbox("parcelable")
    var ParcelableValue: Parcelable? = null

    @Unbox("parcelableArray")
    var parcelableArrayValue: Array<Parcelable>? = null

    @Unbox("sparseParcelableArray")
    var sparseParcelableArrayValue: SparseArray<out Parcelable>? = null

    @Unbox("stringArrayList")
    var stringArrayListValue: List<String>? = null

    @Unbox("parcelableArrayList")
    var parcelableArrayListValue: List<Parcelable>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auto_bundle_result)
        AutoBundle.getDefault().bind(this)
        loginName?.apply {
            Toast.makeText(this@AutoBundleResultActivity, "loginName:" + loginName, Toast.LENGTH_SHORT).show()
        }
    }
}
