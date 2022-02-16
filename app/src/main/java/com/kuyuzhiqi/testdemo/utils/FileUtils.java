package com.kuyuzhiqi.testdemo.utils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;

import java.io.File;

/*****************************************************************************
 * @copyright Copyright (C), 1998-2021, Tencent Tech. Co., Ltd.
 * @file coral_manager.cc
 * @brief    <简单描述>
 * @author guoqunwang
 * @version 1.0.0
 * @date 2021/11/08
 *****************************************************************************/
public class FileUtils {
    public static String getDefaultLogDir(Context context, String logDirName) {
        File logFileDir;
        if (Build.VERSION.SDK_INT >= 30) {
            logFileDir = new File(context.getFilesDir(), logDirName);
        } else {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)
                    && context.getExternalFilesDir(logDirName) != null) {
                logFileDir = context.getExternalFilesDir(logDirName);
            } else {
                logFileDir = new File(context.getFilesDir(), logDirName);
            }
        }
        if (logFileDir != null && !logFileDir.exists()) {
            logFileDir.mkdirs();
        }
        return logFileDir.getAbsolutePath();
    }
}
