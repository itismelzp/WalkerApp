package com.demo.customview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.demo.customview.R;


/**
 * 圆形进度条
 */
public class QFSRoundProgressBar extends View {

    private Rect mTextBounds;
    private Paint mPaint;
    private RectF mOval;

    private float mCurrentProgress;
    private String mContent = "0%";

    // 自定义属性
    private int mTextSize;
    private int mTextColor;
    private int mCircleWidth;
    private int mBgColor;
    private int mCurrentColor;
    private int mLoadSpeed;

    public QFSRoundProgressBar(Context context) {
        this(context, null);
    }

    public QFSRoundProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QFSRoundProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RoundProgressBar, defStyleAttr, 0);
        mTextSize = array.getDimensionPixelSize(R.styleable.RoundProgressBar_textSizeRound,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
        mTextColor = array.getColor(R.styleable.RoundProgressBar_textColorRound, Color.BLACK);
        mBgColor = array.getColor(R.styleable.RoundProgressBar_bgColorRound, Color.BLACK);
        mCircleWidth = array.getDimensionPixelSize(R.styleable.RoundProgressBar_circleWidthRound,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics()));
        mCurrentColor = array.getColor(R.styleable.RoundProgressBar_currentColorRound, Color.BLACK);
        mLoadSpeed = array.getInt(R.styleable.RoundProgressBar_loadSpeedRound, 10);
        array.recycle();

        init();
    }

    private void init() {
        mTextBounds = new Rect();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mOval = new RectF();
    }

    public void setProgress(int percent) {
        mCurrentProgress = percent * 3.6f;
        mContent = Math.round((mCurrentProgress / 360) * 100) + "%";
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setAntiAlias(true);
        mPaint.setColor(mBgColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mCircleWidth);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        // 绘制圆环背景
        int xPoint = getWidth() / 2; // 获取圆心x的坐标
        int radius = xPoint - mCircleWidth; // 获取圆心的半径
        canvas.drawCircle(xPoint, xPoint, radius, mPaint); // 用于定义的圆弧的形状和大小的界限

        // 绘制圆环
        mPaint.setColor(mCurrentColor);
        mOval.set(xPoint - radius, xPoint - radius, radius + xPoint, radius + xPoint);
        canvas.drawArc(mOval, -90, mCurrentProgress, false, mPaint);

        // 绘制当前进度文本
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(0);
        mPaint.setColor(mTextColor);
        mPaint.setTextSize(mTextSize);
        mPaint.getTextBounds(mContent, 0, mContent.length(), mTextBounds);
        canvas.drawText(mContent, xPoint - mTextBounds.width() / 2.f, xPoint + mTextBounds.height() / 2.f, mPaint);

    }

}


