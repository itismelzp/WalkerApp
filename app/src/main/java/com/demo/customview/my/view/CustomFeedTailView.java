package com.demo.customview.my.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
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
    private TextPaint mTextPaint;
    private Path mPath;

    //发光的paint
    private Paint mMaskPaint;
    private BlurMaskFilter mBlurMaskFilter;
    //散射半径
    private float mMaskRadius;
    //背景渐变开始颜色
    private int mStartColor;
    //背景渐变结束颜色
    private int mEndColor;
    //阴影渐变开始颜色
    private int mMaskStartColor;
    //阴影渐变结束颜色
    private int mMaskEndColor;

    //背景圆角和散射的圆角
    private float mRadius = ViewUtils.dpToPx(25);

    private Bitmap mLeftIcon;
    private Drawable mLeftIconDrawable;
    private int mLeftIconSize;
    private String mRightText = "";
    private Bitmap mRightIcon;
    private Drawable mRightIconDrawable;
    private int mRightIconSize;
    private Drawable mBgDrawable;
    private String mTitle;
    private int mTitleColor;
    private int mTitleSize;
    private RectF mRect;
    private Rect mLeftIconRect, mTextRect, mRightTextRect, mRightIconRect;
    private Rect mTextBound, mRightTextRound;

    private int mWidth, mHeight;

    private float mProgress = 0.f;
    private int mDurTime = 500; // ms

    private LinearGradient mGradient = null;
    private float[] mRadii = {
            0f, 0f,
            0f, 0f,
            20f, 20f,
            20f, 20f
    };

    public CustomFeedTailView(Context context) {
        this(context, null);
    }

    public CustomFeedTailView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomFeedTailView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

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
        mPath = new Path();
        mRect = new RectF(); // 整个view的边框
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

        // shader paint
        mMaskPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMaskRadius = ViewUtils.dpToPx(10);
        mStartColor = 0xFFE0563F;
        mEndColor = 0xFFFF7954;
        mMaskStartColor = 0xFFE0563F;
        mMaskEndColor = 0xFFFF7954;

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
//        mTextPaint.setColor(0xFFffffff);
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
                    new int[]{0xFFDFE4FF, 0xFFFFF0FF, 0xFFFFEEED},
                    new float[]{0, .8F, 1.F},
                    Shader.TileMode.CLAMP);
        }
        // set background by shader
//        setMaskPaint();
        setMaskPaint2();
        drawShaderBackground(canvas);

        paint.setShader(mGradient);
        mPath.addRoundRect(mRect, mRadii, Path.Direction.CW);
        canvas.clipPath(mPath);
        canvas.drawRect(mRect, paint);
        paint.setShader(null);

        // set background by drawable
//        mBgDrawable.setBounds(mRect);
//        mBgDrawable.draw(canvas);

        // draw rect
//        drawRectByColor(canvas, paint, mRect, Color.parseColor("#55FF0000"));
    }


    /***
     * 初始化阴影paint
     */
    private void setMaskPaint() {
        LinearGradient maskLinearGradient = new LinearGradient(0, 0, mWidth, 0
                , new int[]{mMaskStartColor, mMaskEndColor}
                , new float[]{0, .9F}
                , Shader.TileMode.CLAMP);
        mMaskPaint.setShader(maskLinearGradient);
        mBlurMaskFilter = new BlurMaskFilter(mMaskRadius, BlurMaskFilter.Blur.NORMAL);
        setLayerType(View.LAYER_TYPE_SOFTWARE, mMaskPaint);
        mMaskPaint.setMaskFilter(mBlurMaskFilter);
    }

    /***
     * 初始化阴影paint
     */
    private void setMaskPaint2() {
        LinearGradient maskLinearGradient = new LinearGradient(0, 0, mWidth, 0
                , new int[]{mMaskStartColor, mMaskEndColor}
                , new float[]{0, .9F}
                , Shader.TileMode.CLAMP);
        mMaskPaint.setShader(maskLinearGradient);
        mMaskPaint.setShadowLayer(mMaskRadius / 2, 0, ViewUtils.dpToPx(1), Color.BLACK);
    }
    /***
     * 初始化阴影paint
     */
    private void setMaskPaint2(Paint paint) {
        LinearGradient maskLinearGradient = new LinearGradient(0, 0, mWidth, 0
                , new int[]{mMaskStartColor, mMaskEndColor}
                , new float[]{0, .9F}
                , Shader.TileMode.CLAMP);
        paint.setShader(maskLinearGradient);
        paint.setShadowLayer(mMaskRadius / 2, 0, ViewUtils.dpToPx(10), Color.BLACK);
    }

    private void drawShaderBackground(Canvas canvas) {
        int x = getPaddingLeft();
        int y = getPaddingTop();
        int right = getWidth() - getPaddingRight();
        int bottom = getHeight() - getPaddingBottom();

        Paint paint = new Paint();
        paint.setColor(0xFFFFFFFF);
        int dx = ViewUtils.dpToPx(4);;
        int dy = ViewUtils.dpToPx(4);
        int blur = ViewUtils.dpToPx(4);
        paint.setShadowLayer(blur, dx, dy, 0x14000000);

        int borderRadius = ViewUtils.dpToPx(10);
        RectF rectF = new RectF(x, y, right, bottom);
        canvas.drawRoundRect(rectF, borderRadius, borderRadius, paint);
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
//        float restWidth = mWidth - mLeftIconRect.width() - mRightIconRect.width() - mRightTextRect.width() - getPaddingLeft() - getPaddingRight();
        float restWidth = mRightTextRect.left - mLeftIconRect.right - ViewUtils.dpToPx(15);
        if (mTextBound.width() > restWidth) {
            mTitle = TextUtils.ellipsize(mTitle, mTextPaint, restWidth, TextUtils.TruncateAt.MIDDLE).toString();
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
        mPaint.setColor(0x55555555);
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
