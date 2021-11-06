package com.demo.customview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.demo.R;
import com.demo.customview.utils.ViewUtils;


/**
 * Created by walkerzpli on 2021/11/2.
 */
public class QCircleAlbumBannerView extends LinearLayout {

    private ImageView mLeftIcon;
    private TextView mFeedAlbumTV;
    private ImageView mRightArrow;
    private float posX, posY;

    public QCircleAlbumBannerView(Context context) {
        this(context, null);
    }

    public QCircleAlbumBannerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QCircleAlbumBannerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.qfs_layer_feed_album_cl, this, true);
        mFeedAlbumTV = contentView.findViewById(R.id.qfs_feed_album_tv);
    }

    public TextView getFeedAlbumTV() {
        return mFeedAlbumTV;
    }

    public void setFeedAlbumTV(TextView mFeedAlbumTV) {
        this.mFeedAlbumTV = mFeedAlbumTV;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean ret = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                posX = event.getX();
                posY = event.getY();
                ret = true;
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(posY - event.getY()) < ViewUtils.dip2px(5)) {
                    ret = true;
                }
                posX = event.getX();
                posY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                if (Math.abs(posY - event.getY()) < ViewUtils.dip2px(5)) {
                    ret = true;
                }
                posX = 0;
                posY = 0;
                break;
        }
        boolean xxx = super.onTouchEvent(event);
        Log.e("CustomMatrixActivity", "o[nTouchEvent] xxx: " + xxx);
        return true;
    }
}
