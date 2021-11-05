package com.kuyuzhiqi.testdemo.ui.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import androidx.appcompat.app.AppCompatActivity;

import com.kuyuzhiqi.testdemo.R;
import com.tencent.mars.xlog.Log;
import com.tencent.mars.xlog.Xlog;

import java.io.File;

public class XLogActivity extends AppCompatActivity {

    private static String PUBLIC_KEY = "bihy05UQQc9KBah079WbBRu4hPqPodYu93Q0Gjzw59PQmMnPFz6mui38TKMxw3PM";
    private static String LOG_DIR_NAME = "xlog";
    private static String msg_512 = "797k4ofwkOg3q6fKMDiEHxC09I0TXBLPkZMccYdRSVW4lBLesSrrGucRFMfet7ocQV7xcuLFgGy3QpAC1JcXv1GjgfQiWJGzg4xrpvJWcqApd34zfnSh7C70wnuQudmKAbK0C6raP57yL0o4lP3EEoDUzPX5OPI2HJzqu0se9WCVjmUECfb84miNUnXZ14lwEqa5vl86JNK0SgTxpY6XaZrk0vkdaAgrqNUD6g69u3bQWQclI1qsX68FEs61Ubu76FwFYsNnANclGDKfeyyevt0fL2MsFhqA4YyCIm8DDPdAL8DZgFmczvFQPHPvPEV4wVonpXKrZO8rZBhEtngmMxVDWW0NZmLNaZ1VtXKnoAqUGp7EjWz8GuNJAW4Ca5s8Q0IZrHg0zGrtfXUa75Or5M2fY7jEnVVMJCzJSg9C4FaNRVBuZTN63werXzcWm6akKM1ydbJYngrozEnxCad4UhueLhsPWuXT6ApQtCoPtwZ3csX1Z82wzZrLVxD7Bh9g";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_xlog);

//        System.loadLibrary("c++_shared");
//        System.loadLibrary("marsxlog");

        final String logPath = getDefaultLogDir(this);
        final String cachePath = getDefaultLogDir(this) + "_cache";

        // 同步日志默认不开启加密模式
//        Xlog.appenderOpen(Xlog.LEVEL_ALL, Xlog.AppednerModeSync, cachePath, logPath, "xlog", 3, PUBLIC_KEY);
        Xlog.open(true, Xlog.LEVEL_DEBUG, Xlog.AppednerModeSync, cachePath, logPath, "log",  PUBLIC_KEY);
        Log.setLogImp(new Xlog());
        findViewById(R.id.btn_test_log).setOnClickListener(v -> {
            long start = System.currentTimeMillis();
            for (int i = 0; i < 100_0000; i++) {
                Log.w("coral", msg_512);
            }
            long diff = System.currentTimeMillis() - start;
            android.util.Log.i("xlog", "加密100w数据耗时：" + diff);
        });
    }

    public static String getDefaultLogDir(Context context) {
        File logFileDir;
        if (Build.VERSION.SDK_INT >= 30) {
            logFileDir = new File(context.getFilesDir(), LOG_DIR_NAME);
        } else {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)
                    && context.getExternalFilesDir(LOG_DIR_NAME) != null) {
                logFileDir = context.getExternalFilesDir(LOG_DIR_NAME);
            } else {
                logFileDir = new File(context.getFilesDir(), LOG_DIR_NAME);
            }
        }
        if (logFileDir != null && !logFileDir.exists()) {
            logFileDir.mkdirs();
        }
        return logFileDir.getAbsolutePath();
    }
}