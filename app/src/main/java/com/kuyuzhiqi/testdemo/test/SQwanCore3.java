package com.kuyuzhiqi.testdemo.test;

import android.util.Log;
import androidx.annotation.NonNull;

/**
 * created by wangguoqun at 2020-02-18
 */
public class SQwanCore3 {
    private static SQwanCore3 instance;
    public static byte[] lock = new byte[0];

    private SQwanCore3() {
    }

    /**
     * 获取SQwanCore单例
     */
    public static SQwanCore3 getInstance() {

        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new SQwanCore3();
                }
            }
        }
        return instance;
    }

    public void onRequestPermissionsResult(int var1, @NonNull String[] var2, @NonNull int[] var3) {
        Log.i("tag","多层调用测试");
    }
}
