package com.demo.customview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.demo.R;

/**
 * Created by walkerzpli on 2020/9/16.
 */
public class MatrixSetPolyToPolyView extends View {

    private Bitmap mBitmap;
    private Matrix mPolyMatrix;

    private int mWidth, mHeight;


    public MatrixSetPolyToPolyView(Context context) {
        this(context, null);
    }

    public MatrixSetPolyToPolyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    private void initBitmapAndMatrix() {

        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.zhaoyun);
        mBitmap = scaleBitmap(mBitmap, mWidth, mHeight);

        mPolyMatrix = new Matrix();

        float[] src = {
                0, 0,                                           // 左上
                mBitmap.getWidth(), 0,                          // 右上
                mBitmap.getWidth(), mBitmap.getHeight(),        // 右下
                0, mBitmap.getHeight()                          // 左下
        };
        float[] dst = {
                0, 0,                                           // 左上
                mBitmap.getWidth(), 400,                        // 右上
                mBitmap.getWidth(), mBitmap.getHeight() - 200,  // 右下
                0, mBitmap.getHeight()                          // 左下
        };

        mPolyMatrix.setPolyToPoly(src, 0, dst, 0, src.length >> 1);

//        mPolyMatrix.postScale(0.26f, 0.26f);
//        mPolyMatrix.postTranslate(0, 200);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initBitmapAndMatrix();
        canvas.drawBitmap(mBitmap, mPolyMatrix, null);
    }

    private Bitmap scaleBitmap(Bitmap origin, int newWidth, int newHeight) {
        if (origin == null) {
            return null;
        }
        int height = origin.getHeight();
        int width = origin.getWidth();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);// 使用后乘
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (!origin.isRecycled()) {
            origin.recycle();
        }
        return newBM;
    }

}
