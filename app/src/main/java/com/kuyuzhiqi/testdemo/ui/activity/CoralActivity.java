package com.kuyuzhiqi.testdemo.ui.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.kuyuzhiqi.testdemo.R;
import com.tencent.coral.CoralInitConfig;
import com.tencent.coral.CoralManager;
import com.tencent.coral.log.CoralLog;
import com.tencent.coral.log.LogConfig;
import com.tencent.coral.log.LogLevel;

public class CoralActivity extends AppCompatActivity {

    boolean flag = true;
    public static String msg_512 = "797k4ofwkOg3q6fKMDiEHxC09I0TXBLPkZMccYdRSVW4lBLesSrrGucRFMfet7ocQV7xcuLFgGy3QpAC1JcXv1GjgfQiWJGzg4xrpvJWcqApd34zfnSh7C70wnuQudmKAbK0C6raP57yL0o4lP3EEoDUzPX5OPI2HJzqu0se9WCVjmUECfb84miNUnXZ14lwEqa5vl86JNK0SgTxpY6XaZrk0vkdaAgrqNUD6g69u3bQWQclI1qsX68FEs61Ubu76FwFYsNnANclGDKfeyyevt0fL2MsFhqA4YyCIm8DDPdAL8DZgFmczvFQPHPvPEV4wVonpXKrZO8rZBhEtngmMxVDWW0NZmLNaZ1VtXKnoAqUGp7EjWz8GuNJAW4Ca5s8Q0IZrHg0zGrtfXUa75Or5M2fY7jEnVVMJCzJSg9C4FaNRVBuZTN63werXzcWm6akKM1ydbJYngrozEnxCad4UhueLhsPWuXT6ApQtCoPtwZ3csX1Z82wzZrLVxD7Bh9g";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coral);

        initSDK();

        initViews();
    }

    private void initSDK() {
        LogConfig logConfig = new LogConfig();
        logConfig.level = LogLevel.kInfo.getCode();
        String logDir = LogConfig.getDefaultLogDir(this);
        logConfig.logDir = logDir;
        logConfig.compress = false;
        logConfig.encrypt = true;
        logConfig.enableLogToFile = true;
        logConfig.enableLogToConsole = true;
        CoralInitConfig coralInitConfig = new CoralInitConfig.Builder()
                .logConfig(logConfig)
                .build();
        CoralManager.init(this, coralInitConfig);
    }

    private void initViews() {
        findViewById(R.id.btn_test_log).setOnClickListener(v -> {
            flag = true;
            for (int i = 0; i < 100_0000; i++) {
                CoralLog.w("coral", msg_512);
            }
            CoralLog.flushFileLog();
        });
        findViewById(R.id.btn_stop_test).setOnClickListener(v -> {
            flag = false;
        });
    }
}