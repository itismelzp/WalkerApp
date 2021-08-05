package com.demo.customview.aige.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.demo.R;

/**
 * Created by walkerzpli on 2020/10/17.
 */
public class PorterDuffView extends View {

    private static final PorterDuff.Mode MODE = PorterDuff.Mode.ADD;

    private static final int RECT_SIZE_SMALL = 400;
    private static final int RECT_SIZE_BIG = 800;

    private Paint mPaint;

    //    private PorterDuffBO porterDuffBO;
    private PorterDuffXfermode porterDuffXfermode;

    private int screenW, screenH;
    private int s_l, s_t;
    private int d_l, d_t;
    private int rectX, rectY;


    public PorterDuffView(Context context) {
        this(context, null);
    }

    public PorterDuffView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mPaint = new Paint();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }
}
