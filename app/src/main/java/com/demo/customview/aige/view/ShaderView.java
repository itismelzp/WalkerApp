package com.demo.customview.aige.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.demo.customview.R;
import com.demo.customview.utils.MeasureUtil;

/**
 * Created by walkerzpli on 2020/9/11.
 */
public class ShaderView extends View {

    private static final int RECT_SIZE_WIDTH = 400;// 矩形尺寸的一半
    private static final int RECT_SIZE_HEIGHT = 400;// 矩形尺寸的一半

    private Paint mPaint;// 画笔
    private SweepGradient mSweepGradient = null;
    private RadialGradient mRadialGradient;

    private int mWidth, mHeight;
    private float mCenterX, mCenterY;
    private int screenX, screenY;

    private int left, top, right, bottom;// 矩形坐上右下坐标

    private Bitmap mBitmap;

    public ShaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 获取屏幕尺寸数据
        int[] screenSize = MeasureUtil.getScreenSize((Activity) context);

        // 获取屏幕中点坐标
        screenX = screenSize[0] / 2;
        screenY = screenSize[1] / 2;

        // 计算矩形左上右下坐标值
        left = screenX - RECT_SIZE_WIDTH;
        top = screenY - RECT_SIZE_HEIGHT;
        right = screenX + RECT_SIZE_WIDTH;
        bottom = screenY + RECT_SIZE_HEIGHT;

        // 实例化画笔
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);

        // 获取位图
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.a);

        LinearGradient gradient = new LinearGradient(left, top, right, bottom,
                new int[]{Color.parseColor("#DFE4FF"), Color.parseColor("#FFF0FF"), Color.parseColor("#FFEEED")},
                new float[]{0, 0.8F, 1.F},
                Shader.TileMode.CLAMP);

        // 实例化一个Shader
        BitmapShader bitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        Matrix matrix = new Matrix();
        matrix.setTranslate(left, top);
        matrix.postRotate(5);
        bitmapShader.setLocalMatrix(matrix);

        // 设置着色器
        mPaint.setShader(bitmapShader);
//        mPaint.setShader(new LinearGradient(left, top, right, bottom, Color.RED, Color.YELLOW, Shader.TileMode.REPEAT));

    }

    @Override
    protected void onDraw(Canvas canvas) {

//        if (mSweepGradient == null) {
//            mSweepGradient = new SweepGradient(mCenterX, mCenterY, Color.RED, Color.YELLOW);
//        }
//
//        if (mRadialGradient == null) {
//            mRadialGradient = new RadialGradient(
//                    mCenterX, mCenterY,
////                    (float) Math.sqrt(mCenterX * mCenterX + mCenterY * mCenterY),
//                    mCenterX,
//                    Color.WHITE, Color.GREEN,
//                    Shader.TileMode.REPEAT
//            );
//        }
//        mPaint.setShader(mRadialGradient);

//        canvas.drawRect(0, 0, mWidth, mHeight, mPaint);
//        mPaint.setShader(null);
//        mPaint.setStyle(Paint.Style.STROKE);
//        mPaint.setStrokeWidth(4);
//        canvas.drawRect(0, 0, mWidth, mHeight, mPaint);
//        canvas.drawBitmap(mBitmap, null, new Rect(left, top, right, bottom),mPaint);

//        canvas.drawRect(left, top, right, bottom, mPaint);
        canvas.drawRect(0, 0, screenX * 2, screenY * 2, mPaint);
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        mWidth = MeasureSpec.getSize(widthMeasureSpec);
//        mHeight = MeasureSpec.getSize(heightMeasureSpec);
//        mCenterX= mWidth / 2f;
//        mCenterY = mHeight / 2f;
//    }

    /**
     * Get Display
     *
     * @param context Context for get WindowManager
     * @return Display
     */
    private static Display getDisplay(Context context) {
        WindowManager wm;
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            wm = activity.getWindowManager();
        } else {
            wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        if (wm != null) {
            return wm.getDefaultDisplay();
        }
        return null;
    }


}
