package com.kuyuzhiqi.testdemo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.animation.DynamicAnimation;
import android.support.animation.SpringAnimation;
import android.support.animation.SpringForce;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import com.kuyuzhiqi.testdemo.R;
import com.kuyuzhiqi.testdemo.utils.DisplayUtils;

//分成三段的视图，默认在最底部
public class ScrollLinearLayout2 extends LinearLayout {

    private float downY;

    private int mActivePointerId;
    private static final int ACTIVE_POINTER = 1;
    private static final int INVALID_POINTER = -1;

    private VelocityTracker mVelocityTracker;
    private int mContentViewId;
    private int mContentViewTranslateTOP;
    private int mContentViewTranslateMiddle;
    private boolean isAnimating = false;
    private int mTouchSlop;
    private int mMaximumVelocity;
    // 中间：1和顶部：2
    private static final int SCROLL_DEFAULT = 0;
    private static final int SCROLL_MIDDLE_TO_TOP = 1;
    private static final int SCROLL_TOP_TO_MIDDLE = 2;
    private static final int SCROLL_BOTTOM_TO_MIDDLE = 3;
    private static final int SCROLL_MIDDLE_TO_BOTTOM = 4;
    private int scrollPosition = SCROLL_DEFAULT;

    private static final int GESTURE_MODE_DEFAULT = 0;
    private static final int GESTURE_MODE_DISABLED = 1;
    private int mGestureMode = GESTURE_MODE_DEFAULT;

    ViewGroup mContentView;
    private int mBlankViewId;
    //blankview不可滚动
    View mBlankView;
    private float mLastY;

    private int mBlankViewHeight = 240;
    protected boolean isExpand = true;
    private int offsetY;

    public ScrollLinearLayout2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledTouchSlop();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ScrollLinearLayout2);
        mContentViewId = array.getResourceId(R.styleable.ScrollLinearLayout2_content_view_id2, 0);

        mBlankViewId = array.getResourceId(R.styleable.ScrollLinearLayout2_blank_view_id2, 0);

        mVelocityTracker = VelocityTracker.obtain();

        mContentViewTranslateTOP = mBlankViewHeight;
        mContentViewTranslateMiddle = mContentViewTranslateTOP / 2;
    }

    @Override public boolean onTouchEvent(MotionEvent event) {
        if (mContentView == null) {
            return false;
        }
        int action = event.getAction();
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
                if (dy < 0 && mContentView.getTranslationY() == -mContentViewTranslateTOP) {
                    mLastY = y;
                    return false;
                }

                //向下滚动，并且到底部
                if (dy > 0 && mContentView.getTranslationY() + dy >= 0) {
                    mContentView.setTranslationY(0);
                    mLastY = y;
                    return super.onTouchEvent(event);
                }

                //向上滑动
                if (dy < 0 && mContentView.getTranslationY() + dy <= -mContentViewTranslateTOP) {
                    mContentView.setTranslationY(-mContentViewTranslateTOP);
                    mLastY = y;
                    return super.onTouchEvent(event);
                }

                mContentView.setTranslationY(mContentView.getTranslationY() + dy);
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
                float transY = Math.abs(mContentView.getTranslationY());
                float convertY = mContentViewTranslateTOP - transY;
                Log.i("tag", "mYVelocity:  " + mYVelocity);

                //向上滑动
                if (mYVelocity < 0) {
                    if (transY > 0 && transY < mContentViewTranslateMiddle) {
                        scrollPosition = SCROLL_BOTTOM_TO_MIDDLE;
                    } else {
                        scrollPosition = SCROLL_MIDDLE_TO_TOP;
                    }
                    shrink();
                } else {
                    //向下滑动
                    if (isTopHalf()) {
                        scrollPosition = SCROLL_TOP_TO_MIDDLE;
                    } else {
                        scrollPosition = SCROLL_MIDDLE_TO_BOTTOM;
                    }
                    expand();
                }
                return super.onTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }

    private boolean isTopHalf() {
        return mContentView.getTranslationY() > -mContentViewTranslateTOP
                && mContentView.getTranslationY() < -mContentViewTranslateMiddle;
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
        float y = ev.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                int index = ev.getActionIndex();
                mActivePointerId = ev.getPointerId(index);
                mLastY = downY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                float dy = y - mLastY;
                if(Math.abs(y - downY) < ViewConfiguration.get(getContext()).getScaledTouchSlop()) {
                    return false;
                }
                //如果向上滚动并且mBlankView已and经收缩则不拦截
                if (dy < 0 && mContentView.getTranslationY() == -mContentViewTranslateTOP) {
                    return false;
                }
                //向下滚动
                if (dy > 0 && mContentView.getTranslationY() == -mContentViewTranslateTOP && y >= DisplayUtils.dipToPx(
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
                            && mContentView.getTranslationY() >= -mContentViewTranslateTOP)) {
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
        if (scrollPosition == SCROLL_TOP_TO_MIDDLE) {
            offsetY = -mContentViewTranslateMiddle;
        } else if (scrollPosition == SCROLL_MIDDLE_TO_BOTTOM) {
            offsetY = 0;
        } else {
            offsetY = -mContentViewTranslateMiddle;
        }
        SpringAnimation springAnimation = new SpringAnimation(mContentView, SpringAnimation.TRANSLATION_Y, offsetY);
        springAnimation.getSpring().setStiffness(SpringForce.STIFFNESS_MEDIUM);
        springAnimation.getSpring().setDampingRatio(SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY);
        springAnimation.setStartVelocity(2000);
        springAnimation.addEndListener(new DynamicAnimation.OnAnimationEndListener() {
            @Override
            public void onAnimationEnd(DynamicAnimation animation, boolean canceled, float value, float velocity) {
                isAnimating = false;
                isExpand = true;
            }
        });
        springAnimation.start();
        isAnimating = true;
        return true;
    }

    private boolean shrink() {
        if (isAnimating || mContentView == null || mBlankView == null) {
            return false;
        }
        if (scrollPosition == SCROLL_BOTTOM_TO_MIDDLE) {
            offsetY = mContentViewTranslateMiddle;
        } else if (scrollPosition == SCROLL_MIDDLE_TO_TOP) {
            offsetY = mContentViewTranslateTOP;
        } else {
            offsetY = mContentViewTranslateMiddle;
        }
        SpringAnimation springAnimation = new SpringAnimation(mContentView, SpringAnimation.TRANSLATION_Y, -offsetY);
        springAnimation.getSpring().setStiffness(SpringForce.STIFFNESS_MEDIUM);
        springAnimation.getSpring().setDampingRatio(SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY);
        springAnimation.setStartVelocity(2000);
        springAnimation.addEndListener(new DynamicAnimation.OnAnimationEndListener() {
            @Override
            public void onAnimationEnd(DynamicAnimation animation, boolean canceled, float value, float velocity) {
                isAnimating = false;
                isExpand = false;
            }
        });
        springAnimation.start();
        isAnimating = true;
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

    @Override protected void onFinishInflate() {
        super.onFinishInflate();
        mContentView = findViewById(mContentViewId);
        if (mContentView != null) {
            mContentView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        }

        mBlankView = findViewById(mBlankViewId);
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

    public void setContentViewTranslateY(int transY) {
        mContentViewTranslateTOP = transY;
        mContentViewTranslateMiddle = mContentViewTranslateTOP / 2;
        requestLayout();
    }
}
