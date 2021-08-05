package com.demo.customview.my.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.demo.R;
import com.demo.customview.utils.ViewUtils;

/**
 * Created by walkerzpli on 2020/9/21.
 */
public class FeedRecomTitleView extends View {

    private int VIEW_WIDTH = ViewUtils.dip2px(358);
    private int VIEW_HEIGHT = ViewUtils.dip2px(71);

    private Drawable mRecomFeedsBg, mRecomContestBg;
    private Drawable mIcon;
    private Drawable mAvatar;

    private Paint mPaint;
    private Rect mRect;
    private RectF mRectF;
    private Rect mTitleBound, mBottomTextBound;
    private Path mPath;

    private String mTitle = "乌云碎大石";
    private String mContestTitle = "我上过的荣耀";

    private String mFansNum = "683W";
    private String mProNum = "8361W";
    private String mBottomPreSuffix = "粉丝    /";
    private String mBottomAfterSuffix = "作品";

    private int mIconSize;
    private int mAvatarSize;

    private boolean isShowRecomFeedHead = true;

    private float[] mRadii = {
            20f, 20f,
            20f, 20f,
            0f, 0f,
            0f, 0f
    };

    public FeedRecomTitleView(Context context) {
        this(context, null);
    }

    public FeedRecomTitleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mIconSize = ViewUtils.dpToPx(20);
        mAvatarSize = ViewUtils.dpToPx(59);

        mPaint = new Paint();
        mRecomFeedsBg = getResources().getDrawable(R.drawable.circle_recommend_feeds_head_bg);
        mRecomContestBg = getResources().getDrawable(R.drawable.circle_recommend_contest_head_bg);
        mIcon = getResources().getDrawable(R.drawable.circle_topic_icon);
        mAvatar = getResources().getDrawable(R.drawable.h001);
        mRect = new Rect();
        mRectF = new RectF();
        mTitleBound = new Rect();
        mBottomTextBound = new Rect();

        mPaint.setTextSize(ViewUtils.dpToPx(17));
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(0xFF000000);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeWidth(2f);
        mPaint.getTextBounds(mTitle, 0, mTitle.length(), mTitleBound);
        mPath = new Path();
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

        if (isShowRecomFeedHead) {
            drawRecomFeedsHead(canvas);
        } else {
            drawRecomContestHead(canvas);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                float x = event.getX();
                float y = event.getY();
                if (isInRect(x, y, mRect)) {
                    isShowRecomFeedHead = !isShowRecomFeedHead;
                    postInvalidate();
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

    private void drawRecomContestHead(Canvas canvas) {
        // draw bg

        setShadowSize(canvas, ViewUtils.dpToPx(2), ViewUtils.dpToPx(2.5f), ViewUtils.dpToPx(2), 0);
        mRecomContestBg.setBounds(0, 0, VIEW_WIDTH, VIEW_HEIGHT);
        mRectF.set(0, 0, VIEW_WIDTH, VIEW_HEIGHT);
        mPath.addRoundRect(mRectF, mRadii, Path.Direction.CW);
        canvas.clipPath(mPath);
        mRecomContestBg.draw(canvas);
        // draw icon
        mRect.left = ViewUtils.dpToPx(10);
        mRect.top = ViewUtils.dpToPx(16);
        mRect.right = mRect.left + mIconSize;
        mRect.bottom = mRect.top + mIconSize;
        mIcon.setBounds(mRect);
        mIcon.draw(canvas);

        // draw title
        mPaint.setTextSize(ViewUtils.dpToPx(17));
        mPaint.setColor(0xFF000000);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeWidth(2f);
        mPaint.getTextBounds(mContestTitle, 0, mContestTitle.length(), mTitleBound);

        int restWidth = VIEW_WIDTH - mRect.right - ViewUtils.dpToPx(10);
        if (mTitleBound.width() > restWidth) {
            mContestTitle = TextUtils.ellipsize(mContestTitle, new TextPaint(mPaint), restWidth, TextUtils.TruncateAt.END).toString();
            mPaint.getTextBounds(mContestTitle, 0, mContestTitle.length(), mTitleBound);
        }

        mRectF.left = mRect.right + ViewUtils.dpToPx(4);
        mRectF.top = VIEW_HEIGHT / 2f - mTitleBound.height() / 2f;
        mRectF.right = mRectF.left + mTitleBound.width();
        mRectF.bottom = VIEW_HEIGHT / 2f - mTitleBound.height() / 2f;
        canvas.drawText(mContestTitle, mRectF.left, getBaseLine(mRectF, mPaint), mPaint);

        // draw bottom text
        drawBottomText(canvas,
                ViewUtils.dpToPx(10),
                ViewUtils.dpToPx(16) + mIconSize + ViewUtils.dpToPx(5));
    }

    private void drawRecomFeedsHead(Canvas canvas) {
        // draw bg

        setShadowSize(canvas, ViewUtils.dpToPx(2), ViewUtils.dpToPx(2.5f), ViewUtils.dpToPx(2), 0);
        mRecomFeedsBg.setBounds(0, 0, VIEW_WIDTH, VIEW_HEIGHT);
        mRectF.set(0, 0, VIEW_WIDTH, VIEW_HEIGHT);
        mPath.addRoundRect(mRectF, mRadii, Path.Direction.CW);
        canvas.clipPath(mPath);
        mRecomFeedsBg.draw(canvas);

        // draw avatar
        mRect.left = ViewUtils.dpToPx(5);
        mRect.top = ViewUtils.dpToPx(4);
        mRect.right = ViewUtils.dpToPx(5) + mAvatarSize;
        mRect.bottom = ViewUtils.dpToPx(4) + mAvatarSize;
        mAvatar.setBounds(mRect);
        mAvatar.draw(canvas);

        // draw title
        mPaint.setTextSize(ViewUtils.dpToPx(17));
        mPaint.setColor(0xFF000000);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeWidth(2f);
        mPaint.getTextBounds(mTitle, 0, mTitle.length(), mTitleBound);
        int restWidth = VIEW_WIDTH - mRect.right - ViewUtils.dpToPx(5);
        if (mTitleBound.width() > restWidth) {
            mTitle = TextUtils.ellipsize(mTitle, new TextPaint(mPaint), restWidth, TextUtils.TruncateAt.END).toString();
            mPaint.getTextBounds(mTitle, 0, mTitle.length(), mTitleBound);
        }

        mRectF.left = ViewUtils.dpToPx(5) + mAvatarSize + ViewUtils.dpToPx(6);
        mRectF.top = ViewUtils.dpToPx(15f);
        mRectF.right = mRectF.left + mTitleBound.width();
        mRectF.bottom = mRectF.top + mTitleBound.height();
        canvas.drawText(mTitle, mRectF.left, getBaseLine(mRectF, mPaint), mPaint);

        // draw bottom text
        drawBottomText(canvas,
                ViewUtils.dpToPx(5) + mAvatarSize + ViewUtils.dpToPx(6),
                ViewUtils.dpToPx(15f) + mTitleBound.height() + ViewUtils.dpToPx(10f));
    }

    private void drawBottomText(Canvas canvas, float left, float top) {

        mPaint.setTextSize(ViewUtils.dpToPx(14f));
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setColor(0xFF000000);
        mPaint.setTextSize(ViewUtils.dpToPx(14f));
        mPaint.getTextBounds(mFansNum, 0, mFansNum.length(), mBottomTextBound);
        mRectF.left = left;
        mRectF.top = top;
        mRectF.right = mRectF.left + mBottomTextBound.width();
        mRectF.bottom = mRectF.top + mBottomTextBound.height();
        canvas.drawText(mFansNum, mRectF.left, getBaseLine(mRectF, mPaint), mPaint);

        mPaint.setTextSize(ViewUtils.dpToPx(12f));
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(0xFF7A7A8D);
        mPaint.getTextBounds(mBottomPreSuffix, 0, (mBottomPreSuffix).length(), mBottomTextBound);
        mRectF.left = mRectF.right + ViewUtils.dpToPx(4f);
        mRectF.right = mRectF.left + mBottomTextBound.width();
        canvas.drawText(mBottomPreSuffix, mRectF.left, getBaseLine(mRectF, mPaint), mPaint);

        mPaint.setTextSize(ViewUtils.dpToPx(14f));
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setColor(0xFF000000);
        mPaint.getTextBounds(mProNum, 0, mProNum.length(), mBottomTextBound);
        mRectF.left = mRectF.right + ViewUtils.dpToPx(12f);
        mRectF.right = mRectF.left + mBottomTextBound.width();
        canvas.drawText(mProNum, mRectF.left, getBaseLine(mRectF, mPaint), mPaint);

        mPaint.setTextSize(ViewUtils.dpToPx(12f));
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(0xFF7A7A8D);
        mPaint.getTextBounds(mBottomAfterSuffix, 0, mBottomAfterSuffix.length(), mBottomTextBound);
        mRectF.left = mRectF.right + ViewUtils.dpToPx(4f);
        mRectF.right = mRectF.left + mBottomTextBound.width();
        canvas.drawText(mBottomAfterSuffix, mRectF.left, getBaseLine(mRectF, mPaint), mPaint);
    }

    // 中文垂直居中
    private float getBaseLine(RectF rect, Paint paint) {
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        return (rect.bottom + rect.top - fontMetrics.bottom - fontMetrics.top - 2) / 2;
    }

    Drawable shadowDrawable = getResources().getDrawable(R.drawable.qzone_adv_single_pic_background_up);
    public void setShadowSize(Canvas canvas, int left, int top, int right, int bottom) {
        int shadowWidth = VIEW_WIDTH + left + right;
        int shadowHeight = VIEW_HEIGHT + top + bottom;
        if (shadowDrawable != null) {
            Rect newShadowBound = new Rect(-ViewUtils.dip2px(4), -ViewUtils.dip2px(3), shadowWidth, shadowHeight);
            shadowDrawable.setBounds(newShadowBound);
            shadowDrawable.draw(canvas);
        }
    }

}
