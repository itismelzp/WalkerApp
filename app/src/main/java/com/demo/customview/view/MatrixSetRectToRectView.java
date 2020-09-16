package com.demo.customview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.demo.customview.R;

/**
 * Created by walkerzpli on 2020/9/16.
 */
public class MatrixSetRectToRectView extends View {

    private int mViewWidth, mViewHeight;
    private Bitmap mBitmap;
    private Matrix mRectMatrix;

    private RectF src;
    private RectF dst;

    public MatrixSetRectToRectView(Context context) {
        this(context, null);
    }

    public MatrixSetRectToRectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.zhaoyun);
        mRectMatrix = new Matrix();
        src = new RectF();
        dst = new RectF();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        mViewHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        src.set(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
        dst.set(0, 0, mViewWidth, mViewHeight);

        mRectMatrix.setRectToRect(src, dst, Matrix.ScaleToFit.CENTER);

        canvas.drawBitmap(mBitmap, mRectMatrix, null);
    }
}
