package com.demo.customview.ryg;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;

import androidx.annotation.Nullable;


import com.demo.customview.utils.ViewUtils;

import java.util.Locale;

public class DispatchView extends View implements GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener {

    private static final String TAG = "DispatchView";

    private final GestureDetector mGestureDetector;


    public DispatchView(Context context) {
        this(context, null);
    }

    public DispatchView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public DispatchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mGestureDetector = new GestureDetector(getContext(), this);
        mGestureDetector.setOnDoubleTapListener(this);
        mGestureDetector.setIsLongpressEnabled(false);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        String log = String.format(Locale.getDefault(),
                "getDensity: %f, getX: %f, getY: %f " +
                        "left: %d, top: %d, right: %d, bottom: %d, width: %d, height: %d",
                ViewUtils.getDensity(),
                getX(), getY(),
                getLeft(), getTop(), getRight(), getBottom(),
                getWidth(), ViewUtils.pxTosp(getHeight())
        );
        Log.d(TAG, log);
    }

    VelocityTracker mVelocityTracker = VelocityTracker.obtain();

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return mGestureDetector.onTouchEvent(event); // 目标View的onTouchEvent方法

//        touchEvent(event);
//        return super.onTouchEvent(event);
    }

    private void touchEvent(MotionEvent event) {
        Log.d(TAG, "=======event start: " + event.getAction() + "=======");
        // MotionEvent TouchSlop
        String log = String.format(Locale.getDefault(),
                "getX: %f, getY: %f, getRawX: %f, getRawY: %f",
                event.getX(), event.getY(), event.getRawX(), event.getRawY()
        );
        Log.d(TAG, log);

        // VelocityTracker GestureDetector Scroller

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        } else {
            mVelocityTracker.clear();
        }

        mVelocityTracker.addMovement(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                // 隐藏在左边的宽度
                int scrollX = getScrollX();
                Log.e(TAG, "V=" + mVelocityTracker.getXVelocity());
                if (Math.abs(mVelocityTracker.getXVelocity()) > 4000f) {

                    int xVelocity = (int) mVelocityTracker.getXVelocity();
                    int yVelocity = (int) mVelocityTracker.getYVelocity();

                    String log2 = String.format(Locale.getDefault(),
                            "xVelocity: %d, yVelocity: %d",
                            xVelocity, yVelocity
                    );
                    Log.d(TAG, log2);

                    if (mVelocityTracker.getXVelocity() < 0f) {
                        // 正向逻辑代码
                    } else {
                        // 反向逻辑代码
                    }
                }

                mVelocityTracker.recycle();
                mVelocityTracker = null;
                break;
            case MotionEvent.ACTION_MOVE:
                // 设置units的值为1000，意思为一秒时间内运动了多少个像素
                mVelocityTracker.computeCurrentVelocity(1000);
                break;
        }
        Log.d(TAG, "=======event end: " + event.getAction() + "=======");
    }


    // OnGestureListener
    @Override
    public boolean onDown(MotionEvent e) {
        Log.d(TAG, "[onDown] e.getAction(): " + e.getAction());
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        Log.d(TAG, "[onShowPress] e.getAction(): " + e.getAction());
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Log.d(TAG, "[onSingleTapUp] e.getAction(): " + e.getAction());
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.d(TAG, "[onFling] e.getAction(): " + e1.getAction());
        Log.d(TAG, "[onFling] e.getAction(): " + e2.getAction());
        Log.d(TAG, "[onFling] distanceX: " + distanceX);
        Log.d(TAG, "[onFling] distanceY: " + distanceY);
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        Log.d(TAG, "[onLongPress] e.getAction(): " + e.getAction());
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.d(TAG, "[onFling] e.getAction(): " + e1.getAction());
        Log.d(TAG, "[onFling] e.getAction(): " + e2.getAction());
        Log.d(TAG, "[onFling] velocityX: " + velocityX);
        Log.d(TAG, "[onFling] velocityY: " + velocityY);
        return false;
    }

    // OnDoubleTapListener
    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        Log.d(TAG, "[onSingleTapConfirmed] e.getAction(): " + e.getAction());
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        Log.d(TAG, "[onDoubleTap] e.getAction(): " + e.getAction());
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        Log.d(TAG, "[onDoubleTapEvent] e.getAction(): " + e.getAction());
        return false;
    }
}
