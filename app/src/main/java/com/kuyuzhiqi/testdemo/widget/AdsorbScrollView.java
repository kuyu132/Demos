package com.kuyuzhiqi.testdemo.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class AdsorbScrollView extends ScrollView {

    private ScrollViewListener mListener;

    public AdsorbScrollView(Context context) {
        super(context);
    }

    public AdsorbScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AdsorbScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AdsorbScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mListener != null) {
            mListener.onScrollChanged(this, l, t, oldl, oldt);
        }
    }

    public void setAdsorbScrollListener(ScrollViewListener listener) {
        mListener = listener;
    }

    public interface ScrollViewListener {
        void onScrollChanged(AdsorbScrollView view, int l, int t, int oldl, int oldt);
    }
}
