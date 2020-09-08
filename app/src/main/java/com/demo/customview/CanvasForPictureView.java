package com.demo.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * Created by walkerzpli on 2020-07-09.
 */
public class CanvasForPictureView extends View {

    private Context mContext;

    private int mWidth, mHeight;
    private Picture mPicture = new Picture();

    public CanvasForPictureView(Context context) {
        this(context, null);
    }

    public CanvasForPictureView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
//        recording();
    }

    private void recording() {

        Canvas canvas = mPicture.beginRecording(500, 5000);

        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL);

        canvas.translate(250, 250);
        canvas.drawCircle(0, 0, 100, paint);
        mPicture.endRecording();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthmode = MeasureSpec.getMode(widthMeasureSpec);      //取出宽度的测量模式
        int heightmode = MeasureSpec.getMode(heightMeasureSpec);    //取出高度的测量模式
        mWidth = MeasureSpec.getSize(widthMeasureSpec);      //取出宽度的确切数值
        mHeight = MeasureSpec.getSize(heightMeasureSpec);    //取出高度的确切数值
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        mPicture.draw(canvas);
//        canvas.drawPicture(mPicture, new RectF(0, 0, mPicture.getWidth(), mPicture.getHeight()));
        canvas.translate(mWidth / 2, mHeight / 2);
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.zhaoyun);
        bitmap = imageScale(bitmap, mWidth, mHeight);
//        canvas.drawBitmap(bitmap, new Matrix(), new Paint());
//        Rect src = new Rect(0, 0, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
//        Rect dst = new Rect(0, 0, 200, 400);
        Rect src = new Rect(0, 0, mWidth / 2, mHeight / 2);
        Rect dst = new Rect(0,0,200,400);
//        canvas.drawBitmap(bitmap, src, dst, null);


        Paint textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(50);
//        canvas.translate(mWidth / 2, mHeight / 2);
//        canvas.drawText("ABCDEFG", 2,5,200, 500, textPaint);

        canvas.drawPosText("ABCDEF", new float[]{
                0, 0,       // 第一个字符位置
                100, 100,   // 第二个字符位置
                200, 0,   // 第三个字符位置
                300, 100,   // ...
                400, 0,
                500, 100,
        }, textPaint);

    }

    public static Bitmap imageScale(Bitmap bitmap, int dst_w, int dst_h) {
        int src_w = bitmap.getWidth();
        int src_h = bitmap.getHeight();
        float scale_w = ((float) dst_w) / src_w;
        float scale_h = ((float) dst_h) / src_h;
        Matrix matrix = new Matrix();
        matrix.postScale(scale_w, scale_h);
        return Bitmap.createBitmap(bitmap, 0, 0, src_w, src_h, matrix,
                true);
    }
}
