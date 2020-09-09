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
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;

import com.demo.customview.R;

/**
 * Created by walkerzpli on 2020/9/8.
 */
public class CustomImageView extends View {

    public static final String TAG = "CustomImageView";

    public static final int IMAGE_SCALE_FITXY = 0;
    public static final int IMAGE_SCALE_CENTER = 1;

    private Bitmap mImage;
    private int mImageScale;
    private String mTitle;
    private int mTextColor;
    private int mTextSize;

    private Rect mRect;
    private Paint mPaint;
    private TextPaint mTextPaint;
    private Rect mTextBound;

    private int mWidth, mHeight;


    public CustomImageView(Context context) {
        this(context, null);
    }

    public CustomImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.getTheme()
                .obtainStyledAttributes(attrs, R.styleable.CustomImageView, defStyleAttr, 0);
        int cnt = array.getIndexCount();
        for (int i = 0; i < cnt; i++) {
            int attr = array.getIndex(i);
            switch (attr) {
                case R.styleable.CustomImageView_image:
                    mImage = BitmapFactory.decodeResource(getResources(), array.getResourceId(attr, 0));
                    break;
                case R.styleable.CustomImageView_imageScaleType:
                    mImageScale = array.getInt(attr, 0);
                    break;
                case R.styleable.CustomImageView_titleText:
                    mTitle = array.getString(attr);
                    break;
                case R.styleable.CustomImageView_titleTextColor:
                    mTextColor = array.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.CustomImageView_titleTextSize:
                    int defTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                            16, getResources().getDisplayMetrics());
                    mTextSize = array.getDimensionPixelSize(attr, defTextSize);
                    break;
                default:
                    break;
            }
        }

        array.recycle();
        mRect = new Rect();
        mTextBound = new Rect();
        mPaint = new Paint();
        mTextPaint = new TextPaint(mPaint);
        mPaint.setTextSize(mTextSize);
        mPaint.getTextBounds(mTitle, 0, mTitle.length(), mTextBound);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY) { // math_parent, accurate
            mWidth = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            int desireWidth = Math.max(mImage.getWidth(), mTextBound.width());
            int desire = getPaddingLeft() + desireWidth + getPaddingRight();
            mWidth = Math.min(desire, specSize);
        }

        specMode = MeasureSpec.getMode(heightMeasureSpec);
        specSize = MeasureSpec.getSize(heightMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            mHeight = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            int desireWith = mImage.getHeight() + mTextBound.height();
            int desire = getPaddingTop() + desireWith + getPaddingBottom();
            mHeight = Math.min(desire, specSize);
        }

        setMeasuredDimension(mWidth, mHeight);
        Log.e(TAG, String.format("mWidth=%d, mHeight=%d, getMeasuredWidth()=%d, getMeasuredHeight()=%d",
                mWidth, mHeight, getMeasuredWidth(), getMeasuredHeight()));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setStrokeWidth(4);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.CYAN);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);

        mRect.left = getPaddingLeft();
        mRect.top = getPaddingTop();
        mRect.right = mWidth - getPaddingRight();
        mRect.bottom = mHeight - getPaddingBottom();

        mPaint.setColor(mTextColor);
        mPaint.setStyle(Paint.Style.FILL);

        if (mTextBound.width() > mWidth) {
            String msg = TextUtils
                    .ellipsize(mTitle, mTextPaint, mWidth - getPaddingLeft() - getPaddingRight(), TextUtils.TruncateAt.END)
                    .toString();
            canvas.drawText(msg, getPaddingLeft(), mHeight - getPaddingBottom(), mPaint);
        } else {
            canvas.drawText(mTitle, mWidth / 2f - mTextBound.width() / 2f, mHeight - getPaddingBottom(), mPaint);
        }

        mRect.bottom -= mTextBound.height();

        if (mImageScale == IMAGE_SCALE_FITXY) {
            canvas.drawBitmap(mImage, null, mRect, mPaint);
        } else {
            mRect.left = mWidth / 2 - mImage.getWidth() / 2;
            mRect.top = (mHeight - mTextBound.height()) / 2 - mImage.getHeight() / 2;
            mRect.right = mWidth / 2 + mImage.getWidth() / 2;
            mRect.bottom = (mHeight - mTextBound.height()) / 2 + mImage.getHeight() / 2;
            canvas.drawBitmap(mImage, null, mRect, mPaint);
        }

    }
}
