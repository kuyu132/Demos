package com.kuyuzhiqi.testdemo.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;

public class DisplayUtils {
    /**
     * dp转px
     *
     * @param context context
     * @param dpValue dp
     * @return px
     */
    public static int dipToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * get the width(in px) of screen
     */
    public static int getScreenWidthPixels(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * get the height(in px) of screen
     */
    public static int getScreenHeightPixels(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 单位：像素
     * @return
     */
    public  static int getStatusBarHeight(Context context) {
        int statusBarHeight = -1;
        //获取status_bar_height资源的ID
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    public static Rect getAppContentRect(Activity activity) {
        Rect contentRect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(contentRect);
        int offset = getStatusBarHeight(activity);
        contentRect.top = offset;
        return contentRect;
    }

    public static int getAppContentHeight(Activity activity) {
        Rect contentRect = getAppContentRect(activity);
        return contentRect.bottom - contentRect.top;
    }
}
