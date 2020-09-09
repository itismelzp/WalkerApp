package com.demo.customview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;

import com.demo.customview.R;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by walkerzpli on 2020/9/8.
 */
public class CustomTitleView extends View {

    public static final String TAG = "CustomTitleView";

    private String mTitleText;
    private int mTitleTextColor;
    private int mTitleTextSize;

    private Rect mBound;
    private Paint mPaint;
    private float mTitleTextWidth;

    public CustomTitleView(Context context) {
        this(context, null);
    }

    public CustomTitleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomTitleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.getTheme()
                .obtainStyledAttributes(attrs, R.styleable.CustomTitleView, defStyleAttr, 0);
        int cnt = array.getIndexCount();

        for (int i = 0; i < cnt; i++) {
            int attr = array.getIndex(i);
            switch (attr) {
                case R.styleable.CustomTitleView_titleText:
                    mTitleText = array.getString(attr);
                    break;
                case R.styleable.CustomTitleView_titleTextColor:
                    mTitleTextColor = array.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.CustomTitleView_titleTextSize:
                    int defTextSize = (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_SP,
                            16,
                            getResources().getDisplayMetrics());
                    mTitleTextSize = array.getDimensionPixelSize(attr, defTextSize);
                    break;
                default:
                    break;
            }
        }
        array.recycle();

        mPaint = new Paint();
        mPaint.setTextSize(mTitleTextSize);
        mBound = new Rect();
        mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mBound);
        mTitleTextWidth = mPaint.measureText(mTitleText);
        setOnClickListener(listener);
    }

    private OnClickListener listener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            mTitleText = randomText();
            postInvalidate(); // 重绘
        }
    };

    private String randomText() {
        Random random = new Random();
        Set<Integer> set = new HashSet<>();
        while (set.size() < 4) {
            int randomInt = random.nextInt(10);
            set.add(randomInt);
        }
        StringBuilder sb = new StringBuilder();
        for (int i : set) {
            sb.append(i);
        }
        return sb.toString();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        printLog(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width, height;

        if (widthMode == MeasureSpec.EXACTLY) { // MATCH_PARENT or 指定大小
            width = widthSize;
        } else {
            // 这里mBound.width()与mTitleTextWidth略有不同，mTitleTextWidth更宽
            width = getPaddingLeft() + (int) mTitleTextWidth + /*mBound.width() + */getPaddingRight();
        }

        if (heightMode == MeasureSpec.EXACTLY) { // MATCH_PARENT or 指定大小
            height = heightSize;
        } else {
            height = getPaddingTop() + mBound.height() + getPaddingBottom();
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(Color.YELLOW);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);

        mPaint.setColor(mTitleTextColor);
        canvas.drawText(mTitleText, getWidth() / 2f - mBound.width() / 2f, getHeight() / 2f + mBound.height() / 2f, mPaint);

    }

    private void printLog(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        String widthModStr = "", heightModeStr = "";
        switch (widthMode) {
            case MeasureSpec.UNSPECIFIED:
                widthModStr = "MeasureSpec.UNSPECIFIED";
                break;
            case MeasureSpec.EXACTLY:
                widthModStr = "MeasureSpec.EXACTLY";
                break;
            case MeasureSpec.AT_MOST:
                widthModStr = "MeasureSpec.AT_MOST";
                break;
        }
        switch (heightMode) {
            case MeasureSpec.UNSPECIFIED:
                heightModeStr = "MeasureSpec.UNSPECIFIED";
                break;
            case MeasureSpec.EXACTLY:
                heightModeStr = "MeasureSpec.EXACTLY";
                break;
            case MeasureSpec.AT_MOST:
                heightModeStr = "MeasureSpec.AT_MOST";
                break;
        }
        Log.e(TAG, String.format("widthMode=%s, heightMode=%s", widthModStr, heightModeStr));
    }

}
