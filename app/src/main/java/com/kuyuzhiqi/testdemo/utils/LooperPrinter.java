package com.kuyuzhiqi.testdemo.utils;

import android.util.Log;
import android.util.Printer;

/**
 * created by wangguoqun at 2020-07-28
 */
public class LooperPrinter implements Printer {
    String TAG = LooperPrinter.class.getSimpleName();
    @Override public void println(String x) {
        Log.i(TAG,"kuyu:"+x);
    }
}
