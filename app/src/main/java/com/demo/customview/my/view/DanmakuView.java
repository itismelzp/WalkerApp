package com.demo.customview.my.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.demo.customview.R;

/**
 * Created by walkerzpli on 2020/9/16.
 */
public class DanmakuView extends View {

    private int mWidth, mHeight;
    private Drawable mBgDrawable;

    public DanmakuView(Context context) {
        this(context, null);
    }

    public DanmakuView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mBgDrawable = context.getResources().getDrawable(R.drawable.qcircle_danmu_bg);
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

        mBgDrawable.setBounds(0, 0, mWidth, mHeight);
        mBgDrawable.draw(canvas);

    }
}
