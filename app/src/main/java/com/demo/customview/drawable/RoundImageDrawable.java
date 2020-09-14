package com.demo.customview.drawable;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by walkerzpli on 2020/9/10.
 */
public class RoundImageDrawable extends Drawable {

    public static final String TAG = "RoundImageDrawable";

    private Paint mPaint;
    private Bitmap mBitmap;
    private RectF mRectF;

    public RoundImageDrawable(Bitmap bitmap) {
        this.mBitmap = bitmap;

        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setShader(bitmapShader);
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
        mRectF = new RectF(left, top, right, bottom);
        Log.e(TAG, "setBounds:" + mRectF);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        Log.e(TAG, "draw:" + mRectF);
        canvas.drawRoundRect(mRectF, 100, 100, mPaint);
    }

    @Override
    public int getIntrinsicWidth() {
        Log.e(TAG, "getIntrinsicWidth:" + mBitmap.getWidth());
        return mBitmap.getWidth();
    }

    @Override
    public int getIntrinsicHeight() {
        Log.e(TAG, "getIntrinsicHeight:" + mBitmap.getHeight());
        return mBitmap.getHeight();
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
