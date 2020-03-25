package com.kuyuzhiqi.testdemo.test;

import androidx.annotation.NonNull;

/**
 * created by wangguoqun at 2020-02-18
 * 多级调用测试
 */
public class SQwanCore {
    private static SQwanCore instance;
    public static byte[] lock = new byte[0];

    private SQwanCore() {
    }

    /**
     * 获取SQwanCore单例
     */
    public static SQwanCore getInstance() {

        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new SQwanCore();
                }
            }
        }
        return instance;
    }

    public void onRequestPermissionsResult(int var1, @NonNull String[] var2, @NonNull int[] var3) {
        SQwanCore2.getInstance().onRequestPermissionsResult(var1, var2, var3);
    }
}
