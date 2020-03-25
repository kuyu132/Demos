package com.kuyuzhiqi.testdemo;

import android.app.Application;
import com.kuyuzhiqi.testdemo.test.BaseApplication;

public class App extends Application {

    public static MsgDisplayListener msgDisplayListener = null;
    public static StringBuilder cacheMsg = new StringBuilder();

    //public App() {
    //    super(
    //            //tinkerFlags, which types is supported
    //            //dex only, library only, all support
    //            ShareConstants.TINKER_ENABLE_ALL,
    //            // This is passed as a string so the shell application does not
    //            // have a binary dependency on your ApplicationLifeCycle class.
    //            "com.kuyuzhiqi.testdemo.App");
    //}

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
