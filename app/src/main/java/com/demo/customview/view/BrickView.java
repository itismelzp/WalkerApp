package com.demo.customview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.demo.customview.R;

/**
 * Created by walkerzpli on 2020/9/12.
 */
public class BrickView extends View {

    private Paint mFillPaint, mStrokePain;
    private BitmapShader mBitmapShader;
    private Bitmap mBitmap;

    private float posX, posY;

    public BrickView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initPint();
    }

    private void initPint() {
        mStrokePain = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mStrokePain.setColor(0xFF000000);
        mStrokePain.setStyle(Paint.Style.STROKE);
        mStrokePain.setStrokeWidth(5);

        mFillPaint = new Paint();

        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.brick);
        mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        mFillPaint.setShader(mBitmapShader);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            posX = event.getX();
            posY = event.getY();

            invalidate();
        }

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawColor(Color.DKGRAY);

        canvas.drawCircle(posX, posY, 300, mFillPaint); // 画圈
        canvas.drawCircle(posX, posY, 300, mStrokePain); // 画砖块
    }
}
