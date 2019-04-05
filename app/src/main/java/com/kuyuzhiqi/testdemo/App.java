package com.kuyuzhiqi.testdemo;

import android.app.Application;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

public class App extends Application {

    @Override public void onCreate() {
        super.onCreate();
    }

    public enum ToastMgr {
        builder;
        private View view;
        private Toast mToast;

        public void init(Context context) {
        }

    }
}
