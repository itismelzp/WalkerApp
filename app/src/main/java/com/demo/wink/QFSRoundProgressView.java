package com.demo.wink;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.demo.R;
import com.demo.customview.utils.ViewUtils;


/**
 * Created by walkerzpli on 2021/7/19.
 */
public class QFSRoundProgressView extends AppCompatImageView {

    private final static String TAG = "QFSRoundProgressView";
    private final static boolean IS_DEBUG_VERSION = true;

    private int mViewWidth;
    private int mViewHeight;

    private Paint mPaint;
    private Path mCornerPath;
    private RectF mRectF;

    private int mCorner;
    private int mBorderWidth;

    private int mMaskColor;
    private int mCornerColor;


    // 进度条属性
    private Rect mTextBounds;
    private RectF mOval;
    private String mProgress;
    private float mStartAngle;
    private float mSweepAngle;
    private int mCenterX;
    private int mCenterY;

    // 进度条属性--自定义属性
    private int mTextSize;
    private int mTextColor;
    private int mCircleWidth;
    private int mBgColor;
    private int mCurrentColor;
    private int mLoadSpeed;
    private float mCircleRadius;

    private int mCurrentProgress;
    private int mStartProgress;
    private int mEndProgress;

    private String mShowTips;
    private int mTipsSize;

    public QFSRoundProgressView(Context context) {
        this(context, null);
    }

    public QFSRoundProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QFSRoundProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initAttr(context, attrs, defStyleAttr);
        initParams();
        initTools();
    }

    private void initAttr(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.QFSRoundProgressView,
                defStyleAttr, 0);
        mTextSize = array.getDimensionPixelSize(R.styleable.QFSRoundProgressView_textSize,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
        mCorner = array.getDimensionPixelSize(R.styleable.QFSRoundProgressView_corner,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics()));
        mBorderWidth = array.getDimensionPixelSize(R.styleable.QFSRoundProgressView_borderWidth,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics()));
        mTextColor = array.getColor(R.styleable.QFSRoundProgressView_textColor, Color.BLACK);
        mBgColor = array.getColor(R.styleable.QFSRoundProgressView_circleBgColor, Color.BLACK);
        mMaskColor = array.getColor(R.styleable.QFSRoundProgressView_maskColor, 0x80000000);
        mCornerColor = array.getColor(R.styleable.QFSRoundProgressView_cornerColor, 0x33FFFFFF);
        mCircleWidth = array.getDimensionPixelSize(R.styleable.QFSRoundProgressView_circleWidth,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics()));
        mCurrentColor = array.getColor(R.styleable.QFSRoundProgressView_circleFgColor, Color.BLACK);
        mLoadSpeed = array.getInt(R.styleable.QFSRoundProgressView_loadSpeed, 10);
        mCircleRadius = array.getDimensionPixelSize(R.styleable.QFSRoundProgressView_circleRadius,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, getResources().getDisplayMetrics()));
        mTipsSize =  ViewUtils.dip2px(9);
        array.recycle();
    }

    private void initParams() {
        mProgress = "0%";
        mStartAngle = -90;
        mCurrentProgress = 0;
        mStartProgress = mEndProgress = 0;
    }

    private void initTools() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCornerPath = new Path();
        mRectF = new RectF();

        mTextBounds = new Rect();
        mOval = new RectF();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewWidth = MeasureSpec.getSize(widthMeasureSpec);
        mViewHeight = MeasureSpec.getSize(heightMeasureSpec);

        mCenterX = mViewWidth >> 1;
        mCenterY = mViewHeight >> 1;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        // 设置圆角
        mCornerPath.reset();
        mRectF.set(getPaddingLeft(), getPaddingTop(),
                mViewWidth - getPaddingRight(), mViewHeight - getPaddingBottom());
        mCornerPath.addRoundRect(mRectF, mCorner, mCorner, Path.Direction.CW);
        mCornerPath.setFillType(Path.FillType.EVEN_ODD);
        canvas.clipPath(mCornerPath);

        // 绘制图片等
        super.onDraw(canvas);

        // 绘制遮罩
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mMaskColor);
        canvas.drawRoundRect(mRectF, mCorner, mCorner, mPaint);

        // 绘制描边
        mPaint.setStrokeWidth(mBorderWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mCornerColor);
        canvas.drawRoundRect(mRectF, mCorner, mCorner, mPaint);

        // 绘制进度条
        drawProgress(canvas);

        // 指定范围刷新
        if (mStartProgress < mEndProgress) {
            mStartProgress++;
            mSweepAngle = mStartProgress * 3.6f;
            mCurrentProgress = mStartProgress;
            mProgress = mStartProgress + "%";
            postInvalidateDelayed(mLoadSpeed);
        }
    }

    private void drawProgress(Canvas canvas) {
        float shiftY = drawTips(canvas);

        // 绘制圆环背景
        mPaint.setColor(mBgColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mCircleWidth);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawCircle(mCenterX, mCenterY, mCircleRadius, mPaint); // 用于定义的圆弧的形状和大小的界限

        // 绘制圆环
        mPaint.setColor(mCurrentColor);
        mOval.set(mCenterX - mCircleRadius, mCenterY - mCircleRadius,
                mCenterX + mCircleRadius, mCenterY + mCircleRadius);
        canvas.drawArc(mOval, mStartAngle, mSweepAngle, false, mPaint);

        // 绘制当前进度文本
        mPaint.setColor(mTextColor);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeWidth(0);
        mPaint.setTextSize(mTextSize);
        mPaint.getTextBounds(mProgress, 0, mProgress.length(), mTextBounds);

        canvas.drawText(mProgress,
                mCenterX - mTextBounds.width() / 2.f,
                mCenterY + mTextBounds.height() / 2.f - shiftY,
                mPaint);
    }

    private float drawTips(Canvas canvas) {
        if(TextUtils.isEmpty(mShowTips)) {
            return 0;
        }
        mPaint.setColor(mTextColor);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeWidth(0);
        mPaint.setTextSize(mTipsSize);
        Rect tipsBounds = new Rect();
        mPaint.getTextBounds(mShowTips, 0, mShowTips.length(), tipsBounds);
        float midH = ((mCircleWidth + mCircleRadius) * 2.75f + tipsBounds.height()) / 2;
        float shiftY = midH - mCircleWidth - mCircleRadius;
        canvas.drawText(mShowTips,
                mCenterX - tipsBounds.width() / 2f,
                mCenterY - shiftY + (mCircleWidth + mCircleRadius) * 1.75f + tipsBounds.height() / 2.f,
                mPaint);
        return shiftY;
    }

    public void setCorner(int corner) {
        this.mCorner = corner;
    }

    public void setCover(Drawable drawable) {
        setImageDrawable(drawable);
    }

    public void setProgress(int progress) {
        checkProgress(progress);
        mSweepAngle = progress * 3.6f;
        mCurrentProgress = progress;
        mProgress = progress + "%";
        postInvalidate();
    }

    /**
     * 传入进度范围[startProgress, endProgress]
     */
    public void setProgressRange(int startProgress, int endProgress) {
        checkProgressRange(startProgress, endProgress);
        this.mStartProgress = startProgress;
        this.mEndProgress = endProgress;
        postInvalidate();
    }

    /**
     * 从当前进度到目标progress
     * @param progress 目标进度
     */
    public void currentToProgress(int progress) {
        setProgressRange(mCurrentProgress, progress);
    }

    private void checkProgress(int progress) {
        if (progress < 0 || progress > 100) {
            if (IS_DEBUG_VERSION) {
                throw new RuntimeException("The progress should be between 0 and 100, progress: " + progress);
            }
        }
    }

    private void checkProgressRange(int startProgress, int endProgress) {
        checkProgress(startProgress);
        checkProgress(endProgress);
        if (startProgress > endProgress) {
            if (IS_DEBUG_VERSION) {
                throw new RuntimeException("The startProgress should be less than endProgress.");
            }
        }
    }

    public void setShowTips(String tips) {
        mShowTips = tips;
    }

}
