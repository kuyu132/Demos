package com.kuyuzhiqi.testdemo;

import android.app.Application;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

public class App extends Application {

    public static MsgDisplayListener msgDisplayListener = null;
    public static StringBuilder cacheMsg = new StringBuilder();

    //hotfix init need attr
    public interface MsgDisplayListener {
        void handle(String msg);
    }

    @Override public void onCreate() {
        super.onCreate();
        initHotfix();
    }

    private void initHotfix() {
        String appVersion;
        try {
            appVersion = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
        } catch (Exception e) {
            appVersion = "1.0.0";
        }

    }
}
