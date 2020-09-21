package com.demo.customview.sloop.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * Created by walkerzpli on 2020/9/19.
 */
public class PathDemoView extends View {

    private int mViewWidth, mViewHeight;

    private Paint mCoorsPaint, mPaint;
    private Path mPath, mSrc;
    private RectF mOval;

    public PathDemoView(Context context) {
        this(context, null);
    }

    public PathDemoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(10);

        DashPathEffect pathEffect = new DashPathEffect(new float[]{10, 5}, 1);
        mCoorsPaint = new Paint();
        mCoorsPaint.setColor(Color.RED);
        mCoorsPaint.setStyle(Paint.Style.STROKE);
        mCoorsPaint.setStrokeWidth(10);
        mCoorsPaint.setPathEffect(pathEffect);

        mPath = new Path();
        mSrc = new Path();
        mOval = new RectF();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewWidth = MeasureSpec.getSize(widthMeasureSpec);
        mViewHeight = MeasureSpec.getSize(heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float centerX = mViewWidth / 2f, centerY = mViewHeight / 2f;
        canvas.translate(centerX, centerY);
        drawCoordinate(centerX, centerY, canvas);

        // draw line test
//        mPath.lineTo(200, 200);
//        mPath.setLastPoint(200, 100);
////        mPath.moveTo(200, 100);
//        mPath.lineTo(200, 0);
//
//        mPath.close();

        // draw rect test
//        mPath.addRect(-200, -200, 200, 200, Path.Direction.CW);
//        mPath.setLastPoint(-300, 300);

        // draw addPath test
//        canvas.scale(1, -1);
//        mPath.addRect(-200, -200, 200, 200, Path.Direction.CW);
//        mSrc.addCircle(0, 0, 100, Path.Direction.CW);
//        mPath.addPath(mSrc, 0, 200);
//        canvas.drawPath(mPath, mPaint);

        // draw addPath test
//        canvas.scale(1, -1);
//        mPath.lineTo(100, 100);
//        mOval.set(0f, 0f, 300f, 300f);
////        mPath.addArc(mOval, 0f, 270f);
//        mPath.arcTo(mOval,0,270, false);

        // draw other test
        canvas.scale(1, -1);
        mPath.addRect(-200, -200, 200, 200, Path.Direction.CW);

        canvas.drawPath(mPath, mPaint);
    }

    private void drawCoordinate(float coordX, float coordY, Canvas canvas) {

        float[] lines = {
                -coordX, 0,
                mViewWidth - coordX, 0,
                0, -coordY,
                0, mViewHeight - coordY
        };

        canvas.drawLines(lines, mCoorsPaint);

    }

}
