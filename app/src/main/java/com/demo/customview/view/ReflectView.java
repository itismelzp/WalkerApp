package com.demo.customview.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.demo.R;
import com.demo.customview.utils.MeasureUtil;

/**
 * Created by walkerzpli on 2020/9/15.
 */
public class ReflectView extends View {

    private Bitmap mSrcBitmap, mRefBitmap;
    private Paint mPaint;
    private PorterDuffXfermode mXfermode; // 混合模式

    private int x, y;

    public ReflectView(Context context) {
        this(context, null);
    }

    public ReflectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initRes(context);

    }

    private void initRes(Context context) {

        mSrcBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.gril);

        Matrix matrix = new Matrix();
        matrix.setScale(1f, -1f);

        // 倒影图
        mRefBitmap = Bitmap.createBitmap(mSrcBitmap, 0, 0, mSrcBitmap.getWidth(), mSrcBitmap.getHeight(), matrix, true);

        int screenW = MeasureUtil.getScreenSize((Activity) context)[0];
        int screenH = MeasureUtil.getScreenSize((Activity) context)[1];

        x = screenW / 2 - mSrcBitmap.getWidth() / 2;
        y = screenH / 2 - mSrcBitmap.getHeight() / 2;

        mPaint = new Paint();
        LinearGradient linearGradient = new LinearGradient(
                x,
                y + mSrcBitmap.getHeight(),
                x,
                y + mSrcBitmap.getHeight() + mSrcBitmap.getHeight() / 4f,
                0xAA000000,
                Color.TRANSPARENT,
                Shader.TileMode.CLAMP);

        mPaint.setShader(linearGradient);
        mXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.BLACK);
        canvas.drawBitmap(mSrcBitmap, x, y, null);
        int sc = canvas.saveLayer(x, y + mSrcBitmap.getHeight(), x + mRefBitmap.getWidth(), y + mSrcBitmap.getHeight() * 2, null, Canvas.ALL_SAVE_FLAG);
        canvas.drawBitmap(mRefBitmap, x, y + mSrcBitmap.getHeight(), null);
        mPaint.setXfermode(mXfermode);
        canvas.drawRect(x, y + mSrcBitmap.getHeight(), x + mRefBitmap.getWidth(), y + mSrcBitmap.getHeight() * 2, mPaint);
        mPaint.setXfermode(null);
        canvas.restoreToCount(sc);

    }
}
