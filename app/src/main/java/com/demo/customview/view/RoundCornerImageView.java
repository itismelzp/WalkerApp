package com.demo.customview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.demo.customview.utils.ViewUtils;

/**
 * 让 GIF 的每一帧都是圆角
 * Created by yarkeyzhang on 2017/8/8.
 */

public class RoundCornerImageView extends PressDarkImageView {

    private static final String TAG = "RoundCornerImageView";
    private Path mPath = new Path();

    private Paint mPaint = new Paint();

    private int mColor = Color.parseColor("#FFD5D5D5");

    private int mBorderWidth;

    private boolean isDrawBorder = false;

    private int corner = ViewUtils.dpToPx(4);


    private int modeIndex = 0;
    private PorterDuffXfermode xfermode[] = {
            //1.所绘制不会提交到画布上。
            new PorterDuffXfermode(PorterDuff.Mode.CLEAR),
            //2.显示上层绘制图片
            new PorterDuffXfermode(PorterDuff.Mode.SRC),
            //3.显示下层绘制图片
            new PorterDuffXfermode(PorterDuff.Mode.DST),
            //4.正常绘制显示，上下层绘制叠盖。
            new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER),
            //5.上下层都显示。下层居上显示。
            new PorterDuffXfermode(PorterDuff.Mode.DST_OVER),
            //6.取两层绘制交集。显示上层。
            new PorterDuffXfermode(PorterDuff.Mode.SRC_IN),
            //7.取两层绘制交集。显示下层。
            new PorterDuffXfermode(PorterDuff.Mode.DST_IN),
            //8.取上层绘制非交集部分。
            new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT),
            //9.取下层绘制非交集部分。
            new PorterDuffXfermode(PorterDuff.Mode.DST_OUT),
            //10.取下层非交集部分与上层交集部分
            new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP),
            //11.取上层非交集部分与下层交集部分
            new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP),
            //12.取两层绘制非交集。两层绘制非交集。
            new PorterDuffXfermode(PorterDuff.Mode.XOR),
            //13.上下层都显示。变暗
            new PorterDuffXfermode(PorterDuff.Mode.DARKEN),
            //14.上下层都显示。变亮
            new PorterDuffXfermode(PorterDuff.Mode.LIGHTEN),
            //15.取两层绘制交集
            new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY),
            //16.上下层都显示。
            new PorterDuffXfermode(PorterDuff.Mode.SCREEN),
    };


    public RoundCornerImageView(Context context) {
        this(context, null, 0);
    }

    public RoundCornerImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundCornerImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mPaint.setColor(mColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setFilterBitmap(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            //当android 版本是 4.1 或者以上时，我们开启硬件加速，使用GPU 去画自动播放的view
            super.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            //当android 版本是 4.0 的时候，某些机型有个bug，view是默认开启硬件加速的（isHardwareAccelerated() 竟然返回 true...），这里强制关闭
            super.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }

    public void setCorner(int px) {
        if (px <= 0) {
            throw new IllegalArgumentException("should not be less than 0");
        }
        corner = px;
    }

    public void setBorder(boolean flag) {

        isDrawBorder = flag;
    }

    public void setColor(int color) {
        mColor = color;
    }

    public void setBorderWidth(int borderWidth) {
        mBorderWidth = borderWidth;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                modeIndex = (modeIndex + 1) % xfermode.length;
                postInvalidate();
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    public void draw(Canvas canvas) {
        if (mPath == null) {
            mPath = new Path();
        }
        mPath.reset();
        final int vWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        final int vHeight = getHeight() - getPaddingTop() - getPaddingBottom();
        RectF rectF = new RectF(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(), getPaddingTop() + vHeight);
        int roundCorner = corner > 0 ? corner : vWidth / 30;
        mPath.addRoundRect(rectF, roundCorner, roundCorner, Path.Direction.CCW);
        mPath.setFillType(Path.FillType.EVEN_ODD);

        canvas.clipPath(mPath);

        super.draw(canvas);

        if (isDrawBorder) {
            mPaint.setColor(mColor);
            if (mBorderWidth != 0) {
                mPaint.setStrokeWidth(mBorderWidth);
            }
            mPath.reset();
            mPath.addRoundRect(rectF, roundCorner, roundCorner, Path.Direction.CCW);
            canvas.drawRoundRect(rectF, roundCorner, roundCorner, mPaint);
        }

        mPaint.setStrokeWidth(ViewUtils.dpToPx(2));
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
//        mPath.addRoundRect(rectF, roundCorner, roundCorner, Path.Direction.CCW);
        mPaint.setColor(0x80000000);
        canvas.drawRoundRect(rectF, roundCorner, roundCorner, mPaint);

        Log.d(TAG, "mPaint.getXfermode(): " + modeIndex);
//        mPaint.setXfermode(xfermode[modeIndex]);


        mPaint.setStyle(Paint.Style.STROKE);
//        mPath.addRoundRect(rectF, roundCorner, roundCorner, Path.Direction.CCW);
        mPaint.setColor(0x33FFFFFF);
        canvas.drawRoundRect(rectF, roundCorner, roundCorner, mPaint);

    }
}
