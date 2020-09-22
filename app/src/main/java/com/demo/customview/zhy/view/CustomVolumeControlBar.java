package com.demo.customview.zhy.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;

import com.demo.customview.R;

/**
 * Created by walkerzpli on 2020/9/9.
 */
public class CustomVolumeControlBar extends View {

    private int mFirstColor;
    private int mSecondColor;
    private int mCircleWidth;
    private Paint mPaint;
    private int mCurCount = 3;
    private Bitmap mImage;
    private int mSplitSize;
    private int mCount;
    private Rect mRect;

    public CustomVolumeControlBar(Context context) {
        this(context, null);
    }

    public CustomVolumeControlBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomVolumeControlBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomVolumeControlBar, defStyleAttr, 0);
        int cnt = array.getIndexCount();
        for (int i = 0; i < cnt; i++) {
            int attr = array.getIndex(i);
            switch (attr) {
                case R.styleable.CustomVolumeControlBar_firstColor:
                    mFirstColor = array.getColor(attr, Color.GREEN);
                    break;
                case R.styleable.CustomVolumeControlBar_secondColor:
                    mSecondColor = array.getColor(attr, Color.CYAN);
                    break;
                case R.styleable.CustomVolumeControlBar_bg:
                    mImage = BitmapFactory.decodeResource(getResources(), array.getResourceId(attr, 0));
                    break;
                case R.styleable.CustomVolumeControlBar_circleWidth:
                    mCircleWidth = array.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_PX, 20, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.CustomVolumeControlBar_dotCount:
                    mCount = array.getInt(attr, 20);// 默认20
                    break;
                case R.styleable.CustomVolumeControlBar_splitSize:
                    mSplitSize = array.getInt(attr, 20);
                    break;
            }
        }

        array.recycle();

        mPaint = new Paint();
        mRect = new Rect();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(mCircleWidth);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStyle(Paint.Style.STROKE);

        int center = getWidth() / 2;
        int radius = center - mCircleWidth;


    }
}
