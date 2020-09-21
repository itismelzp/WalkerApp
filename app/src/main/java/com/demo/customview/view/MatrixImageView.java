package com.demo.customview.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.demo.customview.R;
import com.demo.customview.utils.MeasureUtil;

/**
 * Created by walkerzpli on 2020/9/17.
 */
public class MatrixImageView extends androidx.appcompat.widget.AppCompatImageView {

    private static final String TAG = "MatrixImageView";

    private Bitmap mBitmap;
    private int mWidth, mHeight;

    private int TOUCH_MODE_NONE = 0;
    private int TOUCH_MODE_TRANS = 1;
    private int TOUCH_MODE_ZOOM = 2;

    private int touchMode;

    private float mPreDist;
    private float mPreDegree;

    private boolean isTwoPoints;

    private PointF mFirstPoint, mMidPoint;
    private Matrix mPreMatrix, mCurMatrix;
    private Context mContext;

    private Paint mPaint;

    public MatrixImageView(Context context) {
        super(context);
    }

    public MatrixImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {
        mFirstPoint = new PointF();
        mMidPoint = new PointF();
        mPreMatrix = new Matrix();
        mCurMatrix = new Matrix();
        mPaint = new Paint();

        touchMode = TOUCH_MODE_NONE;
        isTwoPoints = false;

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.zhaoyun);
        bitmap = Bitmap.createScaledBitmap(bitmap, MeasureUtil.getScreenSize((Activity) mContext)[0], MeasureUtil.getScreenSize((Activity) mContext)[1], true);
        setImageBitmap(bitmap);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "MotionEvent.ACTION_DOWN");
                touchMode = TOUCH_MODE_TRANS;
                mPreMatrix.set(mCurMatrix);
                mFirstPoint.set(event.getX(), event.getY());
                isTwoPoints = false;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                Log.d(TAG, "MotionEvent.ACTION_POINTER_DOWN");
                mPreDist = getDist(event);
                if (mPreDist > 10f) {
                    mPreMatrix.set(mCurMatrix);
                    getMidPoint(mMidPoint, event);
                    touchMode = TOUCH_MODE_ZOOM;
                }
                isTwoPoints = true;
                mPreDegree = getDegree(event);
                break;
            case MotionEvent.ACTION_MOVE:
                if (touchMode == TOUCH_MODE_TRANS) {
                    mCurMatrix.set(mPreMatrix);
                    float dx = event.getX() - mFirstPoint.x;
                    float dy = event.getY() - mFirstPoint.y;
                    if (Math.sqrt(dx * dx + dy * dy) > 10f) {
                        mCurMatrix.postTranslate(dx, dy);
                    }
                } else if (touchMode == TOUCH_MODE_ZOOM && event.getPointerCount() == 2) {
                    mCurMatrix.set(mPreMatrix);
                    float curDist = getDist(event);
                    if (curDist > 10f) {
                        mCurMatrix.postScale(curDist / mPreDist, curDist / mPreDist, mMidPoint.x, mMidPoint.y);
                    }
                    if (isTwoPoints) {
                        float curDegree = getDegree(event);
                        mCurMatrix.postRotate(curDegree - mPreDegree, mWidth / 2f, mHeight / 2f);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                Log.d(TAG, "MotionEvent.ACTION_UP");
                Log.d(TAG, "MotionEvent.ACTION_POINTER_UP");
                touchMode = TOUCH_MODE_NONE;
                isTwoPoints = false;
                break;
        }

        setImageMatrix(mCurMatrix);
        return true;
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

        if (mBitmap == null) {
            mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.zhaoyun);
            mBitmap = Bitmap.createScaledBitmap(mBitmap, mWidth, mHeight, true);
        }

        mPaint.setStrokeWidth(4);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.RED);
        canvas.drawRect(0, 0, mWidth, mHeight, mPaint);
        setImageBitmap(mBitmap);
    }

    private float getDegree(MotionEvent event) {
        float dx = event.getX(0) - event.getX(1);
        float dy = event.getY(0) - event.getY(1);
        double ret = Math.atan2(dy, dx);
        return (float) Math.toDegrees(ret);
    }

    private float getDist(MotionEvent event) {
        double x = event.getX(0) - event.getX(1);
        double y = event.getY(0) - event.getY(1);

        return (float) Math.sqrt(x * x + y * y);
    }

    private void getMidPoint(PointF pointF, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        pointF.set(x / 2, y / 2);
    }
}
