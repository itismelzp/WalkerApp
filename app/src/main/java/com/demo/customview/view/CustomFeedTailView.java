package com.demo.customview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;

import com.demo.customview.R;

/**
 * Created by walkerzpli on 2020/9/8.
 */
public class CustomFeedTailView extends View {

    public static final String TAG = "CustomFeedTailView";

    private Paint mPaint;
    private TextPaint mTextPaint;

    private Bitmap mLeftIcon;
    private Bitmap mRightIcon;
    private String mTitle;
    private int mTitleColor;
    private int mTitleSize;
    private Rect mLeftRect, mTextRect, mRightRect;
    private Rect mTextBound;

    private int mWidth, mHeight;

    public CustomFeedTailView(Context context) {
        this(context, null);
    }

    public CustomFeedTailView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomFeedTailView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context
                .getTheme()
                .obtainStyledAttributes(attrs, R.styleable.CustomFeedTailView, defStyleAttr, 0);
        int cnt = array.getIndexCount();
        for (int i = 0; i < cnt; i++) {
            int attr = array.getIndex(i);
            switch (attr) {
                case R.styleable.CustomFeedTailView_leftImage:
                    BitmapFactory.Options opts = new BitmapFactory.Options();
                    opts.outWidth = 10;
                    opts.outHeight = 10;
                    mLeftIcon = BitmapFactory.decodeResource(getResources(), array.getResourceId(attr, 0), opts);
                    break;
                case R.styleable.CustomFeedTailView_rightImage:
                    mRightIcon = BitmapFactory.decodeResource(getResources(), array.getResourceId(attr, 0));
                    break;
                case R.styleable.CustomFeedTailView_titleText:
                    mTitle = array.getString(attr);
                    break;
                case R.styleable.CustomFeedTailView_titleTextColor:
                    mTitleColor = array.getIndex(attr);
                    break;
                case R.styleable.CustomFeedTailView_titleTextSize:
                    int defSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                            16, getResources().getDisplayMetrics());
                    mTitleSize = array.getDimensionPixelSize(attr, defSize);
                    break;
                default:
                    break;
            }
        }

        array.recycle();
        mPaint = new Paint();
        mLeftRect = new Rect();
        mTextRect = new Rect();
        mRightRect = new Rect();
        mTextBound = new Rect();
        mPaint.setTextSize(mTitleSize);
        mPaint.getTextBounds(mTitle, 0, mTitle.length(), mTextBound);
        mTextPaint = new TextPaint(mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            mWidth = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            int desireWidth = getPaddingLeft() + mLeftIcon.getWidth() + mTextBound.width() + mRightIcon.getWidth() + getPaddingRight();
            mWidth = Math.min(desireWidth, specSize);
        }

        specMode = MeasureSpec.getMode(heightMeasureSpec);
        specSize = MeasureSpec.getSize(heightMeasureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            mHeight = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            int desireHeight = Math.max(mLeftIcon.getHeight(), mTextBound.height());
            mHeight = getPaddingLeft() + desireHeight + getPaddingRight();
        }

        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // draw rect
        mPaint.setStrokeWidth(4);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.RED);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);

        // draw left icon
        if (mLeftIcon.getHeight() > mHeight) {
            mLeftRect.left = getPaddingLeft();
            mLeftRect.top = getPaddingTop();
            mLeftRect.right = mHeight + getPaddingLeft();
            mLeftRect.bottom = mHeight - getPaddingBottom();
        } else {
            mLeftRect.left = getPaddingLeft();
            mLeftRect.top = mHeight / 2 - mLeftIcon.getHeight() / 2;
            mLeftRect.right = mLeftRect.left + mLeftIcon.getWidth();
            mLeftRect.bottom = mHeight / 2 + mLeftIcon.getHeight() / 2;
        }
        canvas.drawBitmap(mLeftIcon, null, mLeftRect, mPaint);

        // draw right icon
        if (mRightIcon.getHeight() > mHeight) {
            mRightRect.left = mWidth - getPaddingRight() - mHeight;
            mRightRect.top = getPaddingTop();
            mRightRect.right = mWidth - getPaddingRight();
            mRightRect.bottom = mHeight - getPaddingBottom();
        } else {
            mRightRect.left = mWidth - getPaddingRight() - mRightIcon.getWidth();
            mRightRect.top = mHeight / 2 - mRightIcon.getHeight() / 2;
            mRightRect.right = mWidth - getPaddingRight();
            mRightRect.bottom = mHeight / 2 + mRightIcon.getHeight() / 2;
        }
        canvas.drawBitmap(mRightIcon, null, mRightRect, mPaint);

        // draw text
        mTextRect.left = mLeftRect.right;
        mTextRect.top = mHeight / 2 - mTextBound.height() / 2;
        mTextRect.right = mRightRect.left;
        mTextRect.bottom = mHeight / 2 + mTextBound.height() / 2;
        int textResidueWidth = mWidth - mLeftRect.width() - mRightRect.width() - getPaddingLeft() - getPaddingRight();
        if (mTextBound.width() > textResidueWidth) {
            mTitle = TextUtils.ellipsize(mTitle, mTextPaint, (float) textResidueWidth,
                    TextUtils.TruncateAt.END).toString();
        }
//        canvas.drawRect(mTextRect, mPaint);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.BLACK);
        canvas.drawText(mTitle, mTextRect.left, getBaseLine(), mPaint);
    }

    // 中文垂直居中
    private int getBaseLine() {
        Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();
        return (mTextRect.bottom + mTextRect.top - fontMetrics.bottom - fontMetrics.top) / 2;
    }

}
