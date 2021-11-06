package com.demo.customview.slide_conflict;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.demo.customview.utils.ViewUtils;

/**
 * Created by walkerzpli on 2021/11/6.
 */
public class SeekFrameLayout extends FrameLayout {

    private static final String TAG = "SeekFrameLayout";
    private OnTouchListener listener;

    private int mLastXIntercept = 0;
    private int mLastYIntercept = 0;

    private int mLastX = 0;
    private int mLastY = 0;

    public SeekFrameLayout(@NonNull Context context) {
        super(context);
    }

    public SeekFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SeekFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent event) {
//
//        boolean intercepted = false;
//        int x = (int) event.getX();
//        int y = (int) event.getY();
//
//        String action = "";
//
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN: {
//                intercepted = false;
//                action = "ACTION_DOWN";
//                break;
//            }
//            case MotionEvent.ACTION_MOVE: {
//                int deltaX = x - mLastXIntercept;
//                intercepted = Math.abs(deltaX) > ViewUtils.dip2px(10);
//                action = "ACTION_MOVE";
//                break;
//            }
//            case MotionEvent.ACTION_UP: {
//                intercepted = false;
//                action = "ACTION_UP";
//                break;
//            }
//            default:
//                break;
//        }
//
//        mLastX = x;
//        mLastY = y;
//
//        Log.d(TAG, "[onInterceptTouchEvent] event.getAction(): " + action + ", intercepted: " + intercepted);
//
//        return intercepted;
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
////        int x = (int) event.getX();
////        int y = (int) event.getY();
//        String action = "";
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN: {
//
//                action = "ACTION_DOWN";
//                break;
//            }
//            case MotionEvent.ACTION_MOVE: {
////                int deltaX = x - mLastX;
////                int deltaY = y - mLastY;
//                action = "ACTION_MOVE";
//                break;
//            }
//            case MotionEvent.ACTION_UP: {
//                action = "ACTION_UP";
//                break;
//            }
//            default:
//                break;
//        }
//
//
////        listener.onTouch(this, event);
//        Log.d(TAG, "[onTouchEvent] event.getAction(): " + action);
////        mLastX = x;
////        mLastY = y;
//        return true;
//    }

    //    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//
//        String action = "";
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN: {
//                action = "ACTION_DOWN";
//                break;
//            }
//            case MotionEvent.ACTION_MOVE: {
//                action = "ACTION_MOVE";
//                break;
//            }
//            case MotionEvent.ACTION_UP: {
//                action = "ACTION_UP";
//                break;
//            }
//            default:
//                break;
//        }
//
//        Log.d(TAG, "[onTouchEvent] event.getAction(): " + action);
//        return true;
//    }

    @Override
    public void setOnTouchListener(OnTouchListener l) {
        this.listener = l;
    }
}
