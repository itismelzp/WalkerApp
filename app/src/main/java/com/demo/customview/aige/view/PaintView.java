package com.demo.customview.aige.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.demo.R;

/**
 * Created by walkerzpli on 2020/9/27.
 */
public class PaintView extends View {

    private int mWidth, mHeight;

    private Paint mPaint;
    private Context mContext;
    private Bitmap mBitmap;
    private int x, y;

    public PaintView(Context context) {
        this(context, null);
    }

    public PaintView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        initPaint();
        initRes();
    }

    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColorFilter(new LightingColorFilter(0xFFFF00FF, 0x00000000));
    }

    private void initRes() {
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.a);
        x = mWidth / 2 - mBitmap.getWidth() / 2;
        y = mHeight / 2 - mBitmap.getHeight() / 2;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(mWidth = width, mHeight = height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap, x, y, mPaint);
    }
}
