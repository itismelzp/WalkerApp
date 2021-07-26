package com.demo.wink;

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
 * Created by walkerzpli on 2021/7/19.
 */
public class QFSRoundProgressBar extends View {

    private int VIEW_WIDTH;
    private int VIEW_HEIGHT;

    private Rect mTextBounds;
    private Paint mPaint;
    private RectF mOval;
    private String mProgress = "0%";
    private float mCurrentProgress;

    // 自定义属性
    private int mTextSize;
    private int mTextColor;
    private int mCircleWidth;
    private int mBgColor;
    private int mCurrentColor;
    private int mLoadSpeed;

    private int mCenter;

    public QFSRoundProgressBar(Context context) {
        this(context, null);
    }

    public QFSRoundProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QFSRoundProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RoundProgressBar,
                defStyleAttr, 0);
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

//    public void setProgress(int percent) {
//        mCurrentProgress = percent * 3.6f;
//        mProgress = Math.round((mCurrentProgress / 360) * 100) + "%";
//        postInvalidate();
//    }

    public void setProgress(int progress) {
        mCurrentProgress = progress * 3.6f;
        if (progress < 0 || progress > 1) {
            throw new RuntimeException("The progress should be between 0 and 1.");
        }
        mProgress = progress + "%";
        postInvalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        VIEW_WIDTH = MeasureSpec.getSize(widthMeasureSpec);
        VIEW_HEIGHT = MeasureSpec.getSize(heightMeasureSpec);

        if (VIEW_WIDTH != VIEW_HEIGHT) {
            throw new RuntimeException("The width and height should be the same.");
        }

        mCenter = VIEW_WIDTH >> 1;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 绘制圆环背景
        mPaint.setStrokeWidth(mCircleWidth);
        mPaint.setColor(mBgColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        int radius = mCenter - mCircleWidth;
        canvas.drawCircle(mCenter, mCenter, radius, mPaint);

        // 绘制圆弧
        mPaint.setColor(mCurrentColor);
        mOval.set(mCenter - radius, mCenter - radius, mCenter + radius, mCenter + radius);
        canvas.drawArc(mOval, -90, mCurrentProgress, false, mPaint);

        // 绘制进度文本
        mPaint.setStrokeWidth(0);
        mPaint.setColor(mTextColor);
        mPaint.setTextSize(mTextSize);
        mPaint.getTextBounds(mProgress, 0, mProgress.length(), mTextBounds);
        canvas.drawText(mProgress, mCenter - mTextBounds.width() / 2.f, mCenter + mTextBounds.height() / 2.f, mPaint);

    }

}


