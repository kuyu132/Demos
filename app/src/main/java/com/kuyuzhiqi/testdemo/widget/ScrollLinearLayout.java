package com.kuyuzhiqi.testdemo.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.kuyuzhiqi.testdemo.R;
import com.kuyuzhiqi.testdemo.utils.DisplayUtils;

public class ScrollLinearLayout extends LinearLayout {

    private float downY;

    private int mActivePointerId;
    private static final int ACTIVE_POINTER = 1;
    private static final int INVALID_POINTER = -1;

    private VelocityTracker mVelocityTracker;
    private int mContentViewId;
    private int mContentViewTranslateY;
    private boolean isAnimating = false;
    private int mTouchSlop;
    private int mMaximumVelocity;

    private static final int GESTURE_MODE_DEFAULT = 0;
    private static final int GESTURE_MODE_DISABLED = 1;
    private int mGestureMode = GESTURE_MODE_DEFAULT;

    ViewGroup mContentView;
    private int mBlankViewId;
    View mBlankView;
    private float mLastY;

    private int mBlankViewHeight = 240;
    protected boolean isExpand = true;



    public ScrollLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledTouchSlop();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ScrollLinearLayout);
        mContentViewId = array.getResourceId(R.styleable.ScrollLinearLayout_content_view_id, 0);

        mBlankViewId = array.getResourceId(R.styleable.ScrollLinearLayout_blank_view_id, 0);

        mVelocityTracker = VelocityTracker.obtain();

        mContentViewTranslateY = mBlankViewHeight;
    }

    @Override public boolean onTouchEvent(MotionEvent event) {
        if (mContentView == null) {
            return false;
        }
        int action = event.getAction();
        Log.i("tag", "onTouchEvent:action:" + action);
        float y = event.getY();
        mVelocityTracker.addMovement(event);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                int index = event.getActionIndex();
                mActivePointerId = event.getPointerId(index);
                mLastY = downY = y;
                return true;
            case MotionEvent.ACTION_POINTER_DOWN:
                int indexx = event.getActionIndex();
                mActivePointerId = event.getPointerId(indexx);
                if (mActivePointerId == 0) {
                    mLastY = event.getY(mActivePointerId);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mGestureMode == GESTURE_MODE_DISABLED) {
                    return false;
                }
                getPointerIndex(event, mActivePointerId);
                if (mActivePointerId == INVALID_POINTER) {
                    mLastY = y;
                    mActivePointerId = ACTIVE_POINTER;
                }
                float dy = y - mLastY;
                //向上滚动
                if (dy < 0 && mContentView.getTranslationY() == -mContentViewTranslateY) {
                    mLastY = y;
                    return false;
                }

                //向下滚动，并且到底部
                if (dy > 0 && mContentView.getTranslationY() + dy >= 0) {
                    mContentView.setTranslationY(0);
                    translateBlankView();
                    mLastY = y;
                    return super.onTouchEvent(event);
                }

                //向上滑动
                if (dy < 0 && mContentView.getTranslationY() + dy <= -mContentViewTranslateY) {
                    mContentView.setTranslationY(-mContentViewTranslateY);
                    translateBlankView();
                    mLastY = y;
                    return super.onTouchEvent(event);
                }

                mContentView.setTranslationY(mContentView.getTranslationY() + dy);
                translateBlankView();
                mLastY = y;
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_POINTER_UP:
                int pointerIndex = getPointerIndex(event, mActivePointerId);
                if (mActivePointerId == INVALID_POINTER) {
                    break;
                }
                mLastY = event.getY(pointerIndex);
                break;
            case MotionEvent.ACTION_UP:
                final VelocityTracker velocityTracker = mVelocityTracker;
                velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                float mYVelocity = velocityTracker.getYVelocity();
                if (mContentView.getTranslationY() == 0 || mContentView.getTranslationY() == mContentViewTranslateY) {
                    expand();
                    break;
                }

                if (Math.abs(mYVelocity) >= 800) {
                    if (mYVelocity < 0) {
                        shrink();
                    } else {
                        expand();
                    }
                    return super.onTouchEvent(event);
                }
                if (event.getY() - downY > 0) {
                    expand();
                } else {
                    shrink();
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        Log.i("tag", "||t:" + t);
    }

    @Override public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isAnimating) {
            return true;
        }

        if (mGestureMode == GESTURE_MODE_DISABLED) {
            return false;
        }

        final int action = ev.getAction();
        Log.i("tag", "onInterceptTouchEvent:action:" + action);
        float y = ev.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                int index = ev.getActionIndex();
                mActivePointerId = ev.getPointerId(index);
                mLastY = downY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                float dy = y - mLastY;
                //如果向上滚动并且mBlankView已经收缩则不拦截
                if (dy < 0 && mContentView.getTranslationY() == -mContentViewTranslateY) {
                    return false;
                }
                //向下滚动
                if (dy > 0 && mContentView.getTranslationY() == -mContentViewTranslateY && y >= DisplayUtils.dipToPx(
                        getContext(),
                        98)) {
                    if (!isScrollTop()) {
                        return false;
                    }
                }
                if (dy > 0 && mContentView.getTranslationY() == 0 && y >= DisplayUtils.dipToPx(getContext(), 98)) {
                    return false;
                }
                if (Math.abs(y) > mTouchSlop) {
                    if ((dy > 0 && mContentView.getTranslationY() <= 0) || (dy < 0
                            && mContentView.getTranslationY() >= -mContentViewTranslateY)) {
                        mLastY = y;
                        return true;
                    }
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    private boolean expand() {
        if (isAnimating || mContentView == null || mBlankView == null) {
            return false;
        }

        ObjectAnimator objectAnimator =
                ObjectAnimator.ofFloat(mContentView, "translationY", mContentView.getTranslationY(), 0f);
        objectAnimator.setDuration(240);
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override public void onAnimationUpdate(ValueAnimator animation) {
                float currentValue = (float) animation.getAnimatedValue();
                float percent = currentValue * 1.0f / mContentViewTranslateY;
                mBlankView.setTranslationY(mContentViewTranslateY * percent);
                isAnimating = true;
            }
        });
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isAnimating = false;
                isExpand = true;
            }
        });
        objectAnimator.start();
        return true;
    }

    private boolean shrink() {
        if (isAnimating || mContentView == null || mBlankView == null) {
            return false;
        }

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mContentView, "translationY", -mContentViewTranslateY);
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override public void onAnimationUpdate(ValueAnimator animation) {
                float currentValue = (float) animation.getAnimatedValue();
                float percent = currentValue * 1.0f / mContentViewTranslateY;
                mBlankView.setTranslationY(mContentViewTranslateY * percent);
                isAnimating = true;
            }
        });
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isAnimating = false;
                isExpand = false;
            }
        });
        objectAnimator.setDuration(240);
        objectAnimator.start();
        return true;
    }

    private int getPointerIndex(MotionEvent ev, int id) {
        int activePointerIndex = ev.findPointerIndex(id);
        if (activePointerIndex == -1) {
            mActivePointerId = INVALID_POINTER;
        }
        return activePointerIndex;
    }

    protected boolean isScrollTop() {
        if (mContentView instanceof RecyclerView) {
            return ((RecyclerView) mContentView).computeVerticalScrollOffset() == 0;
        }
        if (mContentView instanceof AbsListView) {
            boolean result = false;
            AbsListView listView = (AbsListView) mContentView;
            if (listView.getFirstVisiblePosition() == 0) {
                final View topChildView = listView.getChildAt(0);
                result = topChildView.getTop() == 0;
            }
            return result;
        }
        return mContentView.getScrollY() == 0;
    }

    private void translateBlankView() {
        float percent = mContentView.getTranslationY() * 1.0f / mContentViewTranslateY;
        mBlankView.setTranslationY(mContentViewTranslateY * percent);
    }

    @Override protected void onFinishInflate() {
        super.onFinishInflate();
        mContentView = findViewById(mContentViewId);
        if (mContentView != null) {
            mContentView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        }

        mBlankView = findViewById(mBlankViewId);
        int count = this.getChildCount();
        for (int i = 0; i < count; i++) {
            Log.i("tag", this.getChildAt(i).toString());
        }
    }

    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mContentView == null) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        int height = MeasureSpec.getSize(heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int heightSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        mContentView.measure(widthMeasureSpec, heightSpec);
        mContentView.layout(mContentView.getLeft(), mContentView.getTop(), mContentView.getHeight(),
                mContentView.getBottom());
    }

    public void setBlankViewHeight(int blankViewHeight) {
        mBlankViewHeight = blankViewHeight;
        mContentViewTranslateY = mBlankViewHeight;
        requestLayout();
    }
}
