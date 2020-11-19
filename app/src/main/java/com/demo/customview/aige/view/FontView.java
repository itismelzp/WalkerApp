package com.demo.customview.aige.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * Created by walkerzpli on 2020/10/17.
 */
public class FontView extends View {

    private static final String TEXT = "ap爱哥ξτβбпшㄎㄊěǔぬも┰┠№＠↓";
    private Paint mPaint;// 画笔
    private Paint textPaint, linePaint;//
    private Paint.FontMetrics mFontMetrics;// 文本测量对象

    private int baseX, baseY;

    private Rect rect;

    public FontView(Context context) {
        super(context);
    }

    public FontView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    private void initPaint() {
        // 实例化画笔
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(50);
        mPaint.setColor(Color.BLACK);

        mFontMetrics = mPaint.getFontMetrics();

        // 实例化画笔
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(50);
        textPaint.setColor(Color.BLACK);

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(1);
        linePaint.setColor(Color.RED);

        rect = new Rect();
        textPaint.getTextBounds(TEXT, 0, TEXT.length(), rect);

        Log.d("walker", "ascent：" + mFontMetrics.ascent);
        Log.d("walker", "top：" + mFontMetrics.top);
        Log.d("walker", "leading：" + mFontMetrics.leading);
        Log.d("walker", "descent：" + mFontMetrics.descent);
        Log.d("walker", "bottom：" + mFontMetrics.bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        baseX = (int) (getWidth() / 2 - textPaint.measureText(TEXT) / 2);
        baseY = (int) (getHeight() / 2f - (textPaint.descent() + textPaint.ascent()) / 2f);
        canvas.drawText(TEXT, baseX, baseY, textPaint);
        canvas.drawRect(rect, linePaint);
        canvas.drawLine(0, getHeight() / 2f, getWidth(), getHeight() / 2f, linePaint);
        canvas.drawLine(0, getHeight() / 2f - rect.width() / 2f, getWidth(), getHeight() / 2f - rect.width() / 2f, linePaint);
        canvas.drawLine(0, getHeight() / 2f + rect.width() / 2f, getWidth(), getHeight() / 2f + rect.width() / 2f, linePaint);


//        canvas.drawText(TEXT, 0, Math.abs(mFontMetrics.top), mPaint);
//        mPaint.setColor(Color.RED);
//        mPaint.setStyle(Paint.Style.STROKE);
//        mPaint.setStrokeWidth(1);
//        canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
//        canvas.drawLine(0, getHeight() / 2f, getWidth(), getHeight() / 2f, mPaint);


    }
}
