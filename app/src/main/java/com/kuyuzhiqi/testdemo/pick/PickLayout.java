package com.kuyuzhiqi.testdemo.pick;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import com.kuyuzhiqi.testdemo.utils.DisplayUtils;
import java.util.ArrayList;
import java.util.List;

public class PickLayout extends ViewGroup {

    public static final int RECYCLE_COUNT = 5;
    private static final float THRESHOLD_VALUE = 0.3f;
    public static final int RIGHT_UPPER = 1;
    public static final int LEFT_LOWER = 2;
    private int DIRECTION = RIGHT_UPPER;
    private int index = 0, realIndex = 0;
    private float downY, disY;
    private boolean isAnimationRunning = false;
    private Context mContext;
    private final float ALPHA_LEVEL1 = 0.6f;
    private final float ALPHA_LEVEL2 = 0.3f;
    private final float SCALE_LEVEL1 = 0.55f;
    private final float SCALE_LEVEL2 = 0.45f;
    private Rect touchArea;
    private int cWidth, cHeight;
    /**
     * 沿对角线标号为：4-》2-》1-》3-》5
     * 1-》3为translationDis1
     * 2-》1为translationDis2
     * 4-》2为translationDis3
     * 3-》5为translationDis4
     * translationDis5为2倍childview高度，用来判断滑动百分比
     */
    private float[] translationDis1 = new float[2];
    private float[] translationDis2 = new float[2];
    private float[] translationDis3 = new float[2];
    private float[] translationDis4 = new float[2];
    private float translationDis5;
    /**
     * 圆1,2,3,4,5中心点
     */
    private float[] centerPoint1 = new float[2];
    private float[] centerPoint2 = new float[2];
    private float[] centerPoint3 = new float[2];
    private float[] centerPoint4 = new float[2];
    private float[] centerPoint5 = new float[2];
    private float percent = 0.00f;
    private List<PointF> mChildTranslation = new ArrayList<>();
    private List<Float> mChildScale = new ArrayList<>();
    private List<Float> mChildAlpha = new ArrayList<>();
    private VelocityTracker mVelocityTracker;
    private int mMaxVelocity;
    private int mPointerId;
    private int mTotalSize;
    private List<Node> circleNodeList = new ArrayList<>();
    private int itemLayoutId;
    private OnScollListener mOnScollListener;
    private boolean isInitlized;
    /**
     * 是否可以操作
     */
    private boolean isTouchable;

    public PickLayout(Context context) {
        super(context);
        init(context);
    }

    public PickLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PickLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        mVelocityTracker = VelocityTracker.obtain();
        mMaxVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
    }

    public void addItems(int size) {
        removeAllViews();
        isInitlized = false;
        mTotalSize = size;
        int sum;

        if (size > RECYCLE_COUNT) {
            sum = RECYCLE_COUNT;
        } else {
            sum = size;
        }
        for (int i = 0; i < sum; i++) {
            View itemView = LayoutInflater.from(getContext()).inflate(itemLayoutId, null);
            addView(itemView);
        }
    }

    public void init(int itemLayoutId) {
        this.itemLayoutId = itemLayoutId;
    }

    @Override public boolean onTouchEvent(MotionEvent event) {
        mVelocityTracker.addMovement(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = event.getRawY();
                mPointerId = event.getPointerId(0);
                break;
            case MotionEvent.ACTION_MOVE:
                disY = event.getRawY() - downY;
                if (disY < 0) {
                    DIRECTION = RIGHT_UPPER;
                } else {
                    DIRECTION = LEFT_LOWER;
                }
                percent = Math.abs(disY) / translationDis5;
                transform();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mVelocityTracker.computeCurrentVelocity(1000, mMaxVelocity);
                float velocityY = mVelocityTracker.getYVelocity(mPointerId);
                if (Math.abs(velocityY) > mMaxVelocity / 4) {
                    startAnimation();
                } else {
                    if (percent < THRESHOLD_VALUE || percent > 1) {
                        backToNormalPosition();
                    } else {
                        startAnimation();
                    }
                }
                mVelocityTracker.clear();
                break;
        }
        return isAnimationRunning ? super.onTouchEvent(event) : true;
    }

    @Override public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!isInTouchArea(event) || !isTouchable) {
                    return false;
                }
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    private void transform() {
        int cCount = getChildCount();
        if (cCount == 0) {
            return;
        }
        if (DIRECTION == RIGHT_UPPER) {
            if (realIndex > 0) {
                int position3 = (int) (circleNodeList.get(index).getPrior().getElement());
                View preChildView = getChildAt(position3);
                preChildView.setTranslationX(
                        mChildTranslation.get(position3).x + translationDis4[0] * percent);
                preChildView.setTranslationY(
                        mChildTranslation.get(position3).y - translationDis4[1] * percent);
            }

            View childView = getChildAt(index);
            childView.setTranslationX(mChildTranslation.get(index).x + translationDis1[0] * percent);
            childView.setTranslationY(mChildTranslation.get(index).y - translationDis1[1] * percent);
            childView.setScaleX(mChildScale.get(index) - 0.45f * percent);
            childView.setScaleY(mChildScale.get(index) - 0.45f * percent);
            childView.setAlpha(mChildAlpha.get(index) - (1 - ALPHA_LEVEL1) * percent);

            int position2 = -1;
            if (mTotalSize > RECYCLE_COUNT) {
                position2 = (int) (circleNodeList.get(index).getNext().getElement());
            } else if (index + 1 < cCount) {
                position2 = index + 1;
            }
            if (position2 >= 0) {
                View childView1 = getChildAt(position2);
                childView1.setTranslationX(mChildTranslation.get(position2).x + translationDis2[0] * percent);
                childView1.setTranslationY(mChildTranslation.get(position2).y + translationDis2[1] * -percent);
                childView1.setScaleX(mChildScale.get(position2) + 0.55f * percent);
                childView1.setScaleY(mChildScale.get(position2) + 0.55f * percent);
                childView1.setAlpha(mChildAlpha.get(position2) + (1 - ALPHA_LEVEL2) * percent);
            }

            int position4 = -1;
            if (mTotalSize > RECYCLE_COUNT) {
                position4 = (int) (circleNodeList.get(index).getNext().getNext().getElement());
            } else if (index + 2 < cCount) {
                position4 = index + 2;
            }
            if (position4 >= 0) {
                View childView2 = getChildAt(position4);
                childView2.setTranslationX(mChildTranslation.get(position4).x + translationDis3[0] * percent);
                childView2.setTranslationY(mChildTranslation.get(position4).y + -translationDis3[1] * percent);
            }
        } else {
            View childView = getChildAt(index);
            childView.setTranslationX(mChildTranslation.get(index).x + translationDis2[0] * -percent);
            childView.setTranslationY(mChildTranslation.get(index).y + translationDis2[1] * percent);
            childView.setScaleX(mChildScale.get(index) - 0.55f * percent);
            childView.setScaleY(mChildScale.get(index) - 0.55f * percent);
            childView.setAlpha(mChildAlpha.get(index) - (1 - ALPHA_LEVEL2) * percent);

            int position4 = -1;
            if (mTotalSize > RECYCLE_COUNT) {
                position4 = (int) circleNodeList.get(index).getNext().getElement();
            } else if (index + 1 < cCount) {
                position4 = index + 1;
            }
            if (position4 >= 0) {
                View childView1 = getChildAt(position4);
                childView1.setTranslationX(mChildTranslation.get(position4).x - translationDis3[0] * percent);
                childView1.setTranslationY(mChildTranslation.get(position4).y + translationDis3[1] * percent);
            }

            if (realIndex > 0) {
                int position1 = (int) circleNodeList.get(index).getPrior().getElement();
                View childView2 = getChildAt(position1);
                childView2.setTranslationX(mChildTranslation.get(position1).x - translationDis1[0] * percent);
                childView2.setTranslationY(mChildTranslation.get(position1).y + translationDis1[1] * percent);
                childView2.setScaleX(mChildScale.get(position1) + 0.45f * percent);
                childView2.setScaleY(mChildScale.get(position1) + 0.45f * percent);
                childView2.setAlpha(mChildAlpha.get(position1) + (1 - ALPHA_LEVEL1) * percent);
            }
            int position3 = -1;
            if (mTotalSize > RECYCLE_COUNT) {
                position3 = (int) circleNodeList.get(index).getPrior().getPrior().getElement();
            } else if (index - 1 > 0) {
                position3 = index - 2;
            }
            if (position3 >= 0) {
                View childView3 = getChildAt(position3);
                childView3.setTranslationX(mChildTranslation.get(position3).x - translationDis4[0] * percent);
                childView3.setTranslationY(mChildTranslation.get(position3).y + translationDis4[1] * percent);
            }
        }
    }

    private void slideToNext() {
        DIRECTION = RIGHT_UPPER;
        startAnimation();
    }

    private void backToNormalPosition() {
        Log.i("tag", "backToNormalPosition");
        int cCount = getChildCount();
        for (int i = 0; i < cCount; i++) {
            View view = getChildAt(i);
            AnimatorSet animatorSet = new AnimatorSet();
            ObjectAnimator translationX =
                    ObjectAnimator.ofFloat(view, "translationX", view.getTranslationX(), mChildTranslation.get(i).x);
            ObjectAnimator translationY =
                    ObjectAnimator.ofFloat(view, "translationY", view.getTranslationY(), mChildTranslation.get(i).y);
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", view.getScaleX(), mChildScale.get(i));
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", view.getScaleY(), mChildScale.get(i));
            ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", view.getAlpha(), mChildAlpha.get(i));
            animatorSet.setDuration(300);
            animatorSet.play(translationX).with(translationY).with(scaleX).with(scaleY).with(alpha);
            animatorSet.start();
        }
    }

    private void startAnimation() {
        if (isAnimationRunning) {
            return;
        }
        if ((realIndex == mTotalSize - 1 && DIRECTION == RIGHT_UPPER)
                || (realIndex == 0 && DIRECTION == LEFT_LOWER)
                || getChildCount() == 0) {
            backToNormalPosition();
            return;
        }
        recordChildViewsTranslation();
        isAnimationRunning = true;
        final float leftPercent = 1 - percent;
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(1f, 100f);
        valueAnimator.addUpdateListener(animation -> {
            if (!isAnimationRunning) {
                return;
            }
            percent = (float) animation.getAnimatedValue() / 100f * leftPercent;
            transform();
        });
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override public void onAnimationStart(Animator animation) {
            }

            @Override public void onAnimationEnd(Animator animation) {
                if (isAnimationRunning) {
                    if (mOnScollListener != null) {
                        mOnScollListener.onPrevious(index);
                    }
                    if (DIRECTION == RIGHT_UPPER) {
                        realIndex++;
                    } else {
                        realIndex--;
                    }
                    if (realIndex < 0) {
                        realIndex = 0;
                    }
                    if (realIndex == mTotalSize) {
                        realIndex = mTotalSize - 1;
                    }
                    index = realIndex % 5;
                    if (mOnScollListener != null) {
                        mOnScollListener.onNext(index);
                    }
                    if (mTotalSize > RECYCLE_COUNT && realIndex + 2 < mTotalSize && realIndex > 1) {
                        recycleUseView();
                    }
                    recordChildViewsTranslation();
                }
                isAnimationRunning = false;
            }

            @Override public void onAnimationCancel(Animator animation) {

            }

            @Override public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.setDuration((long) (1000 * leftPercent > 200 ? 1000 * leftPercent : 200));
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.start();
    }

    private void printAllViewsTrans() {
        int cCount = getChildCount();
        for (int i = 0; i < cCount; i++) {
            View view = getChildAt(i);
            Log.i("tag", "childAtIndex:"
                    + i
                    + " || view:"
                    + " || getTranslationX: "
                    + view.getTranslationX()
                    + " || getTranslationY: "
                    + view.getTranslationY()
                    + " || getScaleX: "
                    + view.getScaleX()
                    + " || getAlpha: "
                    + view.getAlpha());
        }
    }

    private void recycleUseView() {
        if (DIRECTION == RIGHT_UPPER) {
            int position = (int) circleNodeList.get(index).getPrior().getPrior().getPrior().getElement();
            Log.i("tag", "recycleUseView RIGHT_UPPER:position:" + position);
            View view = getChildAt(position);
            view.setTranslationX(centerPoint4[0] - cWidth / 2 - view.getLeft());
            view.setTranslationY(centerPoint4[1] - cHeight / 2 - view.getTop());
            view.setAlpha(ALPHA_LEVEL2);
            view.setScaleX(0.45f);
            view.setScaleY(0.45f);
            if (mOnScollListener != null) {
                mOnScollListener.recycleView(view, RIGHT_UPPER);
            }
        } else {
            int position = (int) circleNodeList.get(index).getNext().getNext().getNext().getElement();
            Log.i("tag", "recycleUseView DIRECTION:position:" + position);
            View view = getChildAt(position);
            view.setTranslationX(centerPoint5[0] - cWidth / 2 - view.getLeft());
            view.setTranslationY(centerPoint5[1] - cHeight / 2 - view.getTop());
            view.setAlpha(ALPHA_LEVEL1);
            view.setScaleX(0.55f);
            view.setScaleY(0.55f);
            mOnScollListener.recycleView(view, LEFT_LOWER);
        }
    }

    private void recordChildViewsTranslation() {
        int cCount = getChildCount();
        reset();
        for (int i = 0; i < cCount; i++) {
            View childView = getChildAt(i);
            mChildTranslation.add(new PointF(childView.getTranslationX(), childView.getTranslationY()));
            mChildScale.add(childView.getScaleX());
            mChildAlpha.add(childView.getAlpha());
        }
    }

    public void removeView() {
        Node node = getCurrentNode();
        Node node1 = node.getNext().getNext();
        View view = getChildAt((Integer) node1.getElement());
        removeView(view);
    }

    public void remove(Node node) {
        View view = getChildAt((Integer) node.getElement());
        removeView(view);
        recordChildViewsTranslation();
        circleNodeList.clear();
        chainNode();
    }

    public void removeHead(Node node) {
        realIndex--;
    }

    public void removeTail(Node node) {

    }

    private void reset() {
        mChildTranslation.clear();
        mChildScale.clear();
        mChildAlpha.clear();
    }

    /**
     * 计算所有ChildView的宽度和高度 然后根据ChildView的计算结果，设置自己的宽和高
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /**
         * 获得此ViewGroup上级容器为其推荐的宽和高，以及计算模式
         */
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

        // 计算出所有的childView的宽和高
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        /**
         * 记录如果是wrap_content是设置的宽和高
         */
        int width = 0;
        int height = 0;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = sizeWidth;
        } else {
            //width = (int) (getChildAt(0).getMeasuredWidth() * 1.5);
            width = (int) (DisplayUtils.dipToPx(getContext(), 240) * 1.5);
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = sizeHeight;
        } else {
            //height = (int) (getChildAt(0).getMeasuredHeight() * 2.4);
            height = (int) (DisplayUtils.dipToPx(getContext(), 240) * 2.4);
        }
        setMeasuredDimension(width, height);
    }

    private boolean isInTouchArea(MotionEvent event) {
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();
        if (touchArea == null) {
            return false;
        }
        if (x < touchArea.right && x > touchArea.left && y > touchArea.top && y < touchArea.bottom) {
            return true;
        }
        return false;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (isInitlized) {
            return;
        }
        int cCount = getChildCount();
        reset();
        circleNodeList.clear();
        if (cCount == 0) {
            return;
        }
        caculatePointAndDistance(getChildAt(0).getMeasuredWidth(), getChildAt(0).getMeasuredHeight());
        if (realIndex == 0) {
            layoutInInitialized();
            View child0 = getChildAt(0);
            touchArea = new Rect(0, child0.getTop(), getWidth(), child0.getBottom());
        } else {
            layoutInPosition();
        }
        chainNode();
        isInitlized = true;
        if (mOnScollListener!= null){
            mOnScollListener.onLayoutDone();
        }
    }

    private void layoutInInitialized() {
        int cCount = getChildCount();
        for (int i = 0; i < cCount; i++) {
            View childView = getChildAt(i);
            int cl = 0, ct = 0, cr = 0, cb = 0;
            if (i == 0) {
                cl = (int) (centerPoint1[0] - cWidth / 2);
                ct = (int) (centerPoint1[1] - cHeight / 2);
            } else if (i == 1) {
                cl = (int) (centerPoint2[0] - cWidth / 2);
                ct = (int) (centerPoint2[1] - cHeight / 2);
                childView.setScaleX(0.45f);
                childView.setScaleY(0.45f);
                childView.setAlpha(ALPHA_LEVEL2);
            } else {
                cl = (int) (centerPoint4[0] - cWidth / 2);
                ct = (int) (centerPoint4[1] - cHeight / 2);
                childView.setScaleX(0.45f);
                childView.setScaleY(0.45f);
                childView.setAlpha(ALPHA_LEVEL2);
            }
            cr = cl + cWidth;
            cb = ct + cHeight;
            childView.layout(cl, ct, cr, cb);
            childView.setScaleX(childView.getScaleX());
            childView.setScaleY(childView.getScaleX());
            mChildTranslation.add(new PointF(0, 0));
            mChildScale.add(childView.getScaleX());
            mChildAlpha.add(childView.getAlpha());
        }
    }

    private void layoutInPosition() {
        int cCount = getChildCount();
        for (int i = 0; i < cCount; i++) {
            View childView = getChildAt(i);
            int cl = 0, ct = 0, cr = 0, cb = 0;
            if (i == index) {
                cl = (int) (centerPoint1[0] - cWidth / 2);
                ct = (int) (centerPoint1[1] - cHeight / 2);
                childView.setScaleX(1f);
                childView.setScaleY(1f);
            } else if (i == index - 1) {
                cl = (int) (centerPoint3[0] - cWidth / 2);
                ct = (int) (centerPoint3[1] - cHeight / 2);
                childView.setScaleX(SCALE_LEVEL1);
                childView.setScaleY(SCALE_LEVEL1);
            } else if (i <= index - 2) {
                cl = (int) (centerPoint5[0] - cWidth / 2);
                ct = (int) (centerPoint5[1] - cHeight / 2);
                childView.setScaleX(SCALE_LEVEL1);
                childView.setScaleY(SCALE_LEVEL1);
            } else if (i == index + 1) {
                cl = (int) (centerPoint2[0] - cWidth / 2);
                ct = (int) (centerPoint2[1] - cHeight / 2);
                childView.setScaleX(SCALE_LEVEL2);
                childView.setScaleY(SCALE_LEVEL2);
            } else if (i >= index + 2) {
                cl = (int) (centerPoint4[0] - cWidth / 2);
                ct = (int) (centerPoint4[1] - cHeight / 2);
                childView.setScaleX(SCALE_LEVEL2);
                childView.setScaleY(SCALE_LEVEL2);
            }
            cr = cl + cWidth;
            cb = ct + cHeight;
            childView.layout(cl, ct, cr, cb);
        }
        recordChildViewsTranslation();
    }

    private void chainNode() {
        for (int i = 0; i < getChildCount(); i++) {
            circleNodeList.add(new Node(i));
        }
        for (int i = 0; i < circleNodeList.size(); i++) {
            if (i == 0) {
                circleNodeList.get(i).setPrior(circleNodeList.get(circleNodeList.size() - 1));
            } else {
                circleNodeList.get(i).setPrior(circleNodeList.get(i - 1));
            }
            if (i == circleNodeList.size() - 1) {
                circleNodeList.get(i).setNext(circleNodeList.get(0));
            } else {
                circleNodeList.get(i).setNext(circleNodeList.get(i + 1));
            }
        }
    }

    private void printAllViewsPos() {
        int cCount = getChildCount();
        for (int i = 0; i < cCount; i++) {
            View view = getChildAt(i);
            Log.i("tag", "childAtIndex:"
                    + i
                    + " || view:"
                    + " || getLeft: "
                    + view.getLeft()
                    + " || getTop: "
                    + view.getTop()
                    + " || getRight: "
                    + view.getRight()
                    + " || getBottom: "
                    + view.getBottom());
        }
    }

    /**
     * 沿对角线标号为：4-》2-》1-》3-》5
     * 1-》3为translationDis1
     * 2-》1为translationDis2
     * 4-》2为translationDis3
     * 3-》5为translationDis4
     */
    private void caculatePointAndDistance(int w, int h) {
        this.cWidth = w;
        this.cHeight = h;
        int circle3Top = (int) (cHeight * 0.55f / 2 - dipToPx(mContext, 10) - cHeight / 2);
        centerPoint1[0] = getWidth() / 2;
        centerPoint1[1] = dipToPx(mContext, 126) + cHeight / 2;
        centerPoint2[0] = cWidth * 0.45f / 2 - dipToPx(mContext, 38);
        centerPoint2[1] = getHeight() / 2 + dipToPx(mContext, 150);
        centerPoint3[0] = getWidth() - cWidth * 0.55f / 2 - dipToPx(mContext, 8);
        centerPoint3[1] = cHeight * 0.55f / 2 - dipToPx(mContext, 10);
        centerPoint4[0] = -cWidth / 2;
        centerPoint4[1] = centerPoint2[1] + cHeight / 2;
        centerPoint5[0] = centerPoint3[0] + cWidth / 4;
        centerPoint5[1] = circle3Top - cHeight / 2;
        translationDis1[0] = Math.abs(centerPoint1[0] - centerPoint3[0]);
        translationDis1[1] = Math.abs(centerPoint1[1] - centerPoint3[1]);
        translationDis2[0] = Math.abs(centerPoint1[0] - centerPoint2[0]);
        translationDis2[1] = Math.abs(centerPoint1[1] - centerPoint2[1]);
        translationDis3[0] = Math.abs(centerPoint2[0] - centerPoint4[0]);
        translationDis3[1] = Math.abs(centerPoint4[1] - centerPoint2[1]);
        translationDis4[0] = Math.abs(centerPoint3[0] - centerPoint5[0]);
        translationDis4[1] = Math.abs(centerPoint3[1] - centerPoint5[1]);
        translationDis5 = cHeight * 2;
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    public int dipToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public void setTouchable(boolean touchable) {
        isTouchable = touchable;
    }

    public int getRealIndex() {
        return realIndex;
    }

    public void setRealIndex(int realIndex) {
        this.realIndex = realIndex;
        if (mTotalSize >= RECYCLE_COUNT) {
            this.index = realIndex % RECYCLE_COUNT;
        } else {
            this.index = realIndex;
        }
    }

    public int getIndex() {
        return index;
    }

    public void setTotalSize(int totalSize) {
        mTotalSize = totalSize;
    }

    public void setOnScollListener(OnScollListener onScollListener) {
        mOnScollListener = onScollListener;
    }

    public Node getCurrentNode() {
        if (index >= 0 && index < circleNodeList.size()) {
            return circleNodeList.get(index);
        }
        return null;
    }

    public interface OnScollListener {
        void onNext(int index);

        void onPrevious(int index);

        void recycleView(View view, int direction);

        void onLayoutDone();
    }
}
