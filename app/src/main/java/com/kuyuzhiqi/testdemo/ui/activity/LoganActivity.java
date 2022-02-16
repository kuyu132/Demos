package com.kuyuzhiqi.testdemo.ui.activity;

import static com.kuyuzhiqi.testdemo.ui.activity.XLogActivity.msg_512;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.dianping.logan.Logan;
import com.dianping.logan.LoganConfig;
import com.kuyuzhiqi.testdemo.R;
import com.kuyuzhiqi.testdemo.utils.FileUtils;

public class LoganActivity extends AppCompatActivity {

    private static String LOG_DIR_NAME = "logan";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logan);

        initLogan();
    }

    private void initLogan() {
        String path = FileUtils.getDefaultLogDir(this, LOG_DIR_NAME);
        String cachePath = FileUtils.getDefaultLogDir(this, LOG_DIR_NAME + "_cache");
        FileUtils.getDefaultLogDir(this, LOG_DIR_NAME);
        LoganConfig config = new LoganConfig.Builder()
                .setMaxFile(Long.MAX_VALUE)
                .setCachePath(cachePath)
                .setPath(path)
                .setEncryptKey16("0123456789012345".getBytes())
                .setEncryptIV16("0123456789012345".getBytes())
                .setDay(7)
                .build();
        Logan.init(config);
        findViewById(R.id.btn_test_logan).setOnClickListener(v -> {
//            long start = System.currentTimeMillis();
            for (int i = 0; i < 1000; i++) {
                Logan.w(msg_512, 512);
            }
//            long diff = System.currentTimeMillis() - start;
//            Log.i("logan", "加密100w数据耗时：" + diff);
        });
        findViewById(R.id.btn_flush).setOnClickListener(v -> {
        });
    }
}