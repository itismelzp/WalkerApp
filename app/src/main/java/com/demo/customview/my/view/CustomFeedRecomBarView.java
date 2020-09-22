package com.demo.customview.my.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.demo.customview.R;
import com.demo.customview.utils.ViewUtils;

/**
 * Created by walkerzpli on 2020/9/22.
 */
public class CustomFeedRecomBarView extends View {

    private int VIEW_WIDTH = ViewUtils.dip2px(358);
    private int VIEW_HEIGHT = ViewUtils.dip2px(71);

    private Paint mPaint;
    private Rect mRect, mIconRect;
    private Rect mTextBound;
    private Drawable mCloseIcon;
    private int mCloseIconSize;

    private String mTitle = "猜你喜欢";

    public CustomFeedRecomBarView(Context context) {
        this(context, null);
    }

    public CustomFeedRecomBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        ViewUtils.initContext(context);

        mRect = new Rect();
        mIconRect = new Rect();
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setTextSize(ViewUtils.dpToPx(14f));
        mPaint.setStrokeWidth(2f);
        mPaint.setColor(0xFF03081A);
        mTextBound = new Rect();
        mPaint.getTextBounds(mTitle, 0, mTitle.length(), mTextBound);

        mCloseIcon = getResources().getDrawable(R.drawable.circle_close_icon);
        mCloseIconSize = ViewUtils.dpToPx(16f);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(VIEW_WIDTH = width, VIEW_HEIGHT = height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // draw title
        mRect.left = ViewUtils.dpToPx(8f);
        mRect.top = VIEW_HEIGHT / 2 - mTextBound.height() / 2;
        mRect.right = ViewUtils.dpToPx(8f) + mTextBound.width();
        mRect.bottom = VIEW_HEIGHT / 2 + mTextBound.height() / 2;
        canvas.drawText(mTitle, mRect.left, getBaseLine(mRect, mPaint), mPaint);

        // draw close icon
        mIconRect.left = VIEW_WIDTH - ViewUtils.dpToPx(8f) - mCloseIconSize;
        mIconRect.top = VIEW_HEIGHT / 2 - mCloseIconSize / 2;
        mIconRect.right = VIEW_WIDTH - ViewUtils.dpToPx(8f);
        mIconRect.bottom = VIEW_HEIGHT / 2 + mCloseIconSize / 2;
        mCloseIcon.setBounds(mIconRect);
        mCloseIcon.draw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                if (isInRect(event.getX(), event.getY(), mIconRect)) {
                    if (getVisibility() == View.VISIBLE) {
                        setVisibility(View.INVISIBLE);
                    } else {
                        setVisibility(View.VISIBLE);
                    }
                }
                break;
            default:
                break;
        }

        return true;
    }

    private boolean isInRect(float x, float y, Rect rect) {
        return rect.left <= x && x <= rect.right
                && rect.top <= y && y <= rect.bottom;
    }

    private float getBaseLine(Rect rect, Paint paint) {
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        return (rect.bottom + rect.top - fontMetrics.bottom - fontMetrics.top - 2) / 2f;
    }
}
