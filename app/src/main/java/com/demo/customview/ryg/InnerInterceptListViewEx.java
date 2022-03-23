package com.demo.customview.ryg;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ListView;

public class InnerInterceptListViewEx extends ListView {
    private static final String TAG = "InnerInterceptListViewEx";

    private InnerInterceptHorizontalScrollViewEx mHorizontalScrollViewEx2;

    // 分别记录上次滑动的坐标
    private int mLastX = 0;
    private int mLastY = 0;

    public InnerInterceptListViewEx(Context context) {
        super(context);
    }

    public InnerInterceptListViewEx(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InnerInterceptListViewEx(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setHorizontalScrollViewEx2(
            InnerInterceptHorizontalScrollViewEx horizontalScrollViewEx2) {
        mHorizontalScrollViewEx2 = horizontalScrollViewEx2;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        Log.d(TAG, " event.getAction():" + event.getAction());

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                Log.d(TAG, "requestDisallowInterceptTouchEvent(true)");
                mHorizontalScrollViewEx2.requestDisallowInterceptTouchEvent(true);
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                int deltaX = x - mLastX;
                int deltaY = y - mLastY;
                Log.d(TAG, "dx:" + deltaX + " dy:" + deltaY);
                if (Math.abs(deltaX) > Math.abs(deltaY)) {
                    Log.d(TAG, "requestDisallowInterceptTouchEvent(false)");
                    mHorizontalScrollViewEx2.requestDisallowInterceptTouchEvent(false);
                }
                break;
            }
            case MotionEvent.ACTION_UP: {
                break;
            }
            default:
                break;
        }

        mLastX = x;
        mLastY = y;
        return super.dispatchTouchEvent(event);
    }

}
