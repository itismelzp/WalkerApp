package com.demo.customview.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Shader;
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
    private static final int RECT_SIZE_HEIGHT = 32;// 矩形尺寸的一半

    private Paint mPaint;// 画笔

    private int left, top, right, bottom;// 矩形坐上右下坐标

    private Bitmap mBitmap;

    public ShaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 获取屏幕尺寸数据
        int[] screenSize = MeasureUtil.getScreenSize((Activity) context);

        // 获取屏幕中点坐标
        int screenX = screenSize[0] / 2;
        int screenY = screenSize[1] / 2;

        // 计算矩形左上右下坐标值
        left = screenX - RECT_SIZE_WIDTH;
        top = screenY - RECT_SIZE_HEIGHT;
        right = screenX + RECT_SIZE_WIDTH;
        bottom = screenY + RECT_SIZE_HEIGHT;

        // 实例化画笔
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);

        // 获取位图
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.a);

        // 设置着色器
//        mPaint.setShader(new BitmapShader(mBitmap, Shader.TileMode.MIRROR, Shader.TileMode.CLAMP));

//        mPaint.setShader(new LinearGradient(left, top, right, bottom, Color.RED, Color.YELLOW, Shader.TileMode.REPEAT));

        /*
        <gradient
        android:angle="0"
        android:centerColor="#FFF0FF"
        android:centerX="0.8"
        android:endColor="#FFEEED"
        android:startColor="#DFE4FF" />
         */

        LinearGradient gradient = new LinearGradient(left, top, right, bottom,
                new int[]{Color.parseColor("#DFE4FF"), Color.parseColor("#FFF0FF"), Color.parseColor("#FFEEED")},
                new float[]{0, 0.8F, 1.F},
                Shader.TileMode.CLAMP);
        mPaint.setShader(gradient);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(left, top, right, bottom, mPaint);
//        canvas.drawBitmap(mBitmap, null, new Rect(left, top, right, bottom),mPaint);
    }

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
