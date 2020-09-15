package com.demo.customview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;

import com.demo.customview.R;
import com.demo.customview.utils.ViewUtils;

/**
 * Created by walkerzpli on 2020/9/8.
 */
public class CustomFeedTailView extends View {

    public static final String TAG = "CustomFeedTailView";

    private Paint mPaint;
//    private Paint mGradientPaint;
    private TextPaint mTextPaint;

    private Bitmap mLeftIcon;
    private Drawable mLeftIconDrawable;
    private int mLeftIconSize;
    private String mRightText;
    private Bitmap mRightIcon;
    private Drawable mRightIconDrawable;
    private int mRightIconSize;
    private Drawable mBgDrawable;
    private String mTitle;
    private int mTitleColor;
    private int mTitleSize;
    private Rect mRect;
    private Rect mLeftIconRect, mTextRect, mRightTextRect, mRightIconRect;
    private Rect mTextBound, mRightTextRound;

    private int mWidth, mHeight;

    private float mProgress = 0.f;
    private int mDurTime = 500; // ms

    private LinearGradient mGradient = null;

    public CustomFeedTailView(Context context) {
        this(context, null);
    }

    public CustomFeedTailView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomFeedTailView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ViewUtils.initContext(context);

        TypedArray array = context
                .getTheme()
                .obtainStyledAttributes(attrs, R.styleable.CustomFeedTailView, defStyleAttr, 0);
        int cnt = array.getIndexCount();
        for (int i = 0; i < cnt; i++) {
            int attr = array.getIndex(i);
            int defSize;
            switch (attr) {
                case R.styleable.CustomFeedTailView_leftIcon:
                    mLeftIcon = BitmapFactory.decodeResource(getResources(), array.getResourceId(attr, 0));
                    mLeftIconDrawable = getResources().getDrawable(array.getResourceId(attr, 0));
                    break;
                case R.styleable.CustomFeedTailView_leftIconSize:
                    defSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                            17, getResources().getDisplayMetrics());
                    mLeftIconSize = array.getDimensionPixelSize(attr, defSize);
                    break;
                case R.styleable.CustomFeedTailView_rightText:
                    mRightText = array.getString(attr);
                    break;
                case R.styleable.CustomFeedTailView_rightIcon:
                    mRightIcon = BitmapFactory.decodeResource(getResources(), array.getResourceId(attr, 0));
                    mRightIconDrawable = getResources().getDrawable(array.getResourceId(attr, 0));
                    break;
                case R.styleable.CustomFeedTailView_rightIconSize:
                    defSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                            12, getResources().getDisplayMetrics());
                    mRightIconSize = array.getDimensionPixelSize(attr, defSize);
                    break;
                case R.styleable.CustomFeedTailView_titleText:
                    mTitle = array.getString(attr);
                    break;
                case R.styleable.CustomFeedTailView_titleTextColor:
                    mTitleColor = array.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.CustomFeedTailView_titleTextSize:
                    defSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                            16, getResources().getDisplayMetrics());
                    mTitleSize = array.getDimensionPixelSize(attr, defSize);
                    break;
                case R.styleable.CustomFeedTailView_background:
                    mBgDrawable = getResources().getDrawable(array.getResourceId(attr, 0));
                    break;
                default:
                    break;
            }
        }

        array.recycle();
        mPaint = new Paint();
//        mGradientPaint = new Paint();

        mRect = new Rect(); // 整个view的边框
        mLeftIconRect = new Rect();
        mTextRect = new Rect();
        mRightTextRect = new Rect();
        mRightIconRect = new Rect();
        mTextBound = new Rect();
        mRightTextRound = new Rect();
        mPaint.setTextSize(mTitleSize);
        mPaint.getTextBounds(mTitle, 0, mTitle.length(), mTextBound);
        mPaint.getTextBounds(mRightText, 0, mRightText.length(), mRightTextRound);
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
            int desireWidth = getPaddingLeft() + mLeftIconSize + mTextBound.width() + mRightIconSize + getPaddingRight();
            mWidth = Math.min(desireWidth, specSize);
        }

        specMode = MeasureSpec.getMode(heightMeasureSpec);
        specSize = MeasureSpec.getSize(heightMeasureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            mHeight = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            int desireHeight = Math.max(mLeftIconSize, mTextBound.height());
            mHeight = getPaddingLeft() + desireHeight + getPaddingRight();
        }

        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // draw bg
        drawBackGround(canvas, mPaint);
        // draw left icon
        drawLeftIcon(canvas, mPaint);
        // draw right icon
        drawRightIcon(canvas, mPaint);

        // init paint
        mTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mTextPaint.setColor(mTitleColor);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStrokeWidth(0.5f); // 控制字体加粗的程度
        // draw right text
        drawRightText(canvas, mTextPaint);
        // draw text
        drawText(canvas, mTextPaint);

//        drawProgress(canvas); // 进度条
    }

    private void drawBackGround(Canvas canvas, Paint paint) {
        mRect.set(0, 0, mWidth, mHeight);

        if (mGradient == null) {
            mGradient = new LinearGradient(0, 0, mWidth, mHeight,
                    new int[]{Color.parseColor("#DFE4FF"), Color.parseColor("#FFF0FF"), Color.parseColor("#FFEEED")},
                    new float[]{0, 0.8F, 1.F},
                    Shader.TileMode.CLAMP);
        }
        // set background by shader
        paint.setShader(mGradient);
        canvas.drawRect(mRect, paint);
        paint.setShader(null);

        // set background by drawable
//        mBgDrawable.setBounds(mRect);
//        mBgDrawable.draw(canvas);

        // draw rect
//        drawRectByColor(canvas, paint, mRect, Color.parseColor("#55FF0000"));
    }

    private void drawLeftIcon(Canvas canvas, Paint paint) {
        if (mLeftIconSize > mHeight) {
            mLeftIconRect.left = getPaddingLeft();
            mLeftIconRect.top = getPaddingTop();
            mLeftIconRect.right = mHeight + getPaddingLeft();
            mLeftIconRect.bottom = mHeight - getPaddingBottom();
        } else {
            mLeftIconRect.left = getPaddingLeft() + ViewUtils.dpToPx(7);
            mLeftIconRect.top = mHeight / 2 - mLeftIconSize / 2;
            mLeftIconRect.right = mLeftIconRect.left + mLeftIconSize;
            mLeftIconRect.bottom = mHeight / 2 + mLeftIconSize / 2;
        }
//        canvas.drawBitmap(mLeftIcon, null, mLeftIconRect, mPaint);

        mLeftIconDrawable.setBounds(mLeftIconRect);
        mLeftIconDrawable.draw(canvas);

//        drawRectByColor(canvas, paint, mLeftIconRect, Color.parseColor("#5500FF00"));
    }

    private void drawRightIcon(Canvas canvas, Paint paint) {
        if (mRightIconSize > mHeight) {
            mRightIconRect.left = mWidth - getPaddingRight() - mHeight;
            mRightIconRect.top = getPaddingTop();
            mRightIconRect.right = mWidth - getPaddingRight();
            mRightIconRect.bottom = mHeight - getPaddingBottom();
        } else {
            mRightIconRect.left = mWidth - mRightIconSize - ViewUtils.dpToPx(6) - getPaddingRight();
            mRightIconRect.top = mHeight / 2 - mRightIconSize / 2;
            mRightIconRect.right = mWidth - ViewUtils.dpToPx(6) - getPaddingRight();
            mRightIconRect.bottom = mHeight / 2 + mRightIconSize / 2;
        }
        canvas.drawBitmap(mRightIcon, null, mRightIconRect, mPaint);

        mRightIconDrawable.setBounds(mRightIconRect);
        mRightIconDrawable.draw(canvas);
//        drawRectByColor(canvas, paint, mRightIconRect, Color.parseColor("#550000FF"));
    }

    private void drawRightText(Canvas canvas, Paint paint) {
        // draw text
        mRightTextRect.left = mRightIconRect.left - mRightTextRound.width() - ViewUtils.dpToPx(6);
        mRightTextRect.top = mHeight / 2 - mRightTextRound.height() / 2;
        mRightTextRect.right = mRightIconRect.left - ViewUtils.dpToPx(6);
        mRightTextRect.bottom = mHeight / 2 + mRightTextRound.height() / 2;
        canvas.drawText(mRightText, mRightTextRect.left, getBaseLine(mRightTextRect, paint), paint);
//        drawRectByColor(canvas, paint, mRightTextRect, Color.parseColor("#5500FF00"));
    }

    private void drawText(Canvas canvas, Paint paint) {
        // draw text
        mTextRect.left = mLeftIconRect.right + ViewUtils.dpToPx(7);
        mTextRect.top = mHeight / 2 - mTextBound.height() / 2;
        mTextRect.right = mRightTextRect.left;
        mTextRect.bottom = mHeight / 2 + mTextBound.height() / 2;
        float restWidth = mWidth - mLeftIconRect.width() - mRightIconRect.width() - mRightTextRect.width() - getPaddingLeft() - getPaddingRight();
        if (mTextBound.width() > restWidth) {
            mTitle = TextUtils.ellipsize(mTitle, mTextPaint, restWidth, TextUtils.TruncateAt.END).toString();
        }
        canvas.drawText(mTitle, mTextRect.left, getBaseLine(mTextRect, paint), paint);
//        drawRectByColor(canvas, paint, mTextRect, Color.parseColor("#550000FF"));
    }

    private void drawRectByColor(Canvas canvas, Paint paint, Rect rect, int color) {
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(color);
        canvas.drawRect(rect, paint);
    }

    private void drawProgress(Canvas canvas) {
        mPaint.setColor(Color.parseColor("#55555555"));
        mRect.set(0, 0, (int) (mWidth * mProgress), mHeight);
        canvas.drawRect(mRect, mPaint);
        mProgress += 1f / mDurTime;

        if (mProgress < 1f) {
            postInvalidate();
        }
    }

    // 中文垂直居中
    private int getBaseLine(Rect rect, Paint paint) {
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        return (rect.bottom + rect.top - fontMetrics.bottom - fontMetrics.top - 2) / 2;
    }

}
