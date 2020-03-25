package com.kuyuzhiqi.testdemo.test;

import androidx.annotation.NonNull;

/**
 * created by wangguoqun at 2020-02-18
 */
public class SQwanCore2 {
    private static SQwanCore2 instance;
    public static byte[] lock = new byte[0];

    private SQwanCore2() {
    }

    /**
     * 获取SQwanCore单例
     */
    public static SQwanCore2 getInstance() {

        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new SQwanCore2();
                }
            }
        }
        return instance;
    }

    public void onRequestPermissionsResult(int var1, @NonNull String[] var2, @NonNull int[] var3) {
        SQwanCore3.getInstance().onRequestPermissionsResult(var1, var2, var3);
    }
}
