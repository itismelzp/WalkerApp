package com.demo.customview.drawable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.demo.R;

/**
 * Created by walkerzpli on 2020/9/30.
 */
public class ClipDrawableView extends View {

    public static final String TAG = "ClipDrawableView";

    private int mWidth, mHeight;

    private Paint mPaint;
    private Drawable drawable;
    private Bitmap bitmap;
    private ClipDrawable clipDrawable;
    private Rect mSrc, mDest;
    private int clipLevel = 0;
    private int CLIP_LEVEL = 10000;
    private int imgWidth, imgHeight;

    public ClipDrawableView(Context context) {
        this(context, null);
    }

    public ClipDrawableView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        drawable = getResources().getDrawable(R.drawable.zhaoyun2);
        clipDrawable = new ClipDrawable(drawable, Gravity.CENTER, ClipDrawable.VERTICAL);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.zhaoyun2);
        clipDrawable.setLevel(10000);

        imgWidth = bitmap.getWidth();
        imgHeight = bitmap.getHeight();

        mSrc = new Rect();
        mDest = new Rect();
        mPaint = new Paint();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mWidth = MeasureSpec.getSize(widthMeasureSpec), mHeight = MeasureSpec.getSize(heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setStrokeWidth(4);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(0, 0, mWidth, mHeight, mPaint);
        drawable.setBounds(0, calQCircleSharedImgTop(imgWidth, imgHeight, mWidth), mWidth, calQCircleSharedImgBottom(imgWidth, imgHeight, mWidth));
        drawable.draw(canvas);

//        drawable.setBounds(0, 0, mWidth, mHeight * clipLevel / CLIP_LEVEL);
//        drawable.setLevel(clipLevel);
//        drawable.draw(canvas);
//        clipDrawable.setBounds(0, 0, mWidth, mHeight);
//        clipDrawable.setLevel(clipLevel);
//        clipDrawable.draw(canvas);


//        int clipWidth = imgWidth;
//        int clipHeight = imgWidth * mHeight / mWidth;
//
//        mSrc.set(0,
//                imgHeight / 2 - clipHeight / 2,
//                clipWidth,
//                imgHeight / 2 + clipHeight / 2);
//        mDest.set(0, 0, mWidth, mHeight);
//        canvas.drawBitmap(bitmap, mSrc, mDest, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                if (clipLevel < CLIP_LEVEL) {
                    clipLevel = (int) (clipLevel + .25f * CLIP_LEVEL);
                } else {
                    clipLevel = 0;
                }
                invalidate();
                break;
        }

        return true;
    }

    private int calClipLevel(int imgWidth, int imgHeight, int viewWidth) {

        float needWidth = viewWidth;
        float needHeight = (float) viewWidth / imgWidth * imgHeight;

        float clipHeight = viewWidth * 1.13f;

        int clipLevel = 0;
        if (needHeight > clipHeight) {
            clipLevel = (int) (clipHeight / needHeight * CLIP_LEVEL);
        }
        return clipLevel;
    }

    private int calQCircleSharedImgTop(int imgWidth, int imgHeight, int viewWidth) {
        float needHeight = (float) viewWidth / imgWidth * imgHeight;
        float clipHeight = viewWidth * 1.13f;
        int diffTop = 0;
        if (needHeight > clipHeight) {
            diffTop = (int) (clipHeight / 2 - needHeight / 2);
        }
        return diffTop;
    }

    private int calQCircleSharedImgBottom(int imgWidth, int imgHeight, int viewWidth) {
        float needHeight = (float) viewWidth / imgWidth * imgHeight;
        float clipHeight = viewWidth * 1.13f;
        int diffBottom = 0;
        if (needHeight > clipHeight) {
            diffBottom = (int) (clipHeight / 2 + needHeight / 2);
        }
        return diffBottom;
    }

}
