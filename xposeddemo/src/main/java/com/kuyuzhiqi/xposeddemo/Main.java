package com.kuyuzhiqi.xposeddemo;

import android.util.Log;
import android.widget.TextView;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class Main implements IXposedHookLoadPackage {
    @Override public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        XposedBridge.log("xposed回调");
        if (loadPackageParam.packageName.equals("com.kuyuzhiqi.testdemo01")) {
            XposedHelpers.findAndHookMethod("com.kuyuzhiqi.testdemo01.ui.LoginActivity", loadPackageParam.classLoader,
                    "isCorrectInfo", String.class, String.class, new XC_MethodHook() {
                        @Override protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            super.beforeHookedMethod(param);
                            XposedBridge.log("登录劫持前");
                            XposedBridge.log("用户名:" + param.args[0]);
                            XposedBridge.log("密码:" + param.args[1]);
                        }

                        @Override protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);
                            XposedBridge.log("登录劫持后");
                            XposedBridge.log("用户名:" + param.args[0]);
                            XposedBridge.log("密码:" + param.args[1]);
                        }
                    });
        }
        XposedBridge.log("xposed回调完成");
    }
}
