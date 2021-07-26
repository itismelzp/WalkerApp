package com.demo.customview.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * press 变暗
 * Created by yarkeyzhang on 2015/9/23.
 */
@TargetApi(14)
public class PressDarkImageView extends AppCompatImageView {

    public static final int[] ATTRS = new int[]{
            android.R.attr.fromAlpha,
            android.R.attr.toAlpha
    };
    public static final int SOURCE_QQSTORY = 0;
    public static final int SOURCE_HOMEWORK_TROOP = 1;

    public float mFromAlpha = 1.0f;
    public float mToAlpha = 0.5f;

    public int mSource = SOURCE_QQSTORY;

    public PressDarkImageView(Context context) {
        this(context, null, 0);
    }

    public PressDarkImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PressDarkImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs, ATTRS);
        mFromAlpha = a.getFloat(0, mFromAlpha);
        mToAlpha = a.getFloat(1, mToAlpha);
        a.recycle();
    }

    /**
     * 在有复用场景下，出现过不少状态不对的问题，这里提供个补丁接口
     * http://tapd.oa.com/10066461/bugtrace/bugs/view?bug_id=1010066461065882051&url_cache_key=5e56862a47b3eb623f95c9e02343ecf7
     */
    public void resetUnPressState() {
        if (mSource == SOURCE_HOMEWORK_TROOP) {
            super.clearColorFilter();
        } else if (Build.VERSION.SDK_INT >= 16) {
            super.setImageAlpha((int) (mFromAlpha * 0xFF));
        } else if (Build.VERSION.SDK_INT >= 11) {
            super.setAlpha(mFromAlpha);
        } else {
            super.clearColorFilter();
        }
    }

    /* yarkeyzhang :
     * 重写此函数的话k可以保证图片变暗与背景色同步。
     * 但是要考虑父布局 shouldDelayChildPressedState() 的返回值，
     * 在Scrollable-Container中会有延迟情况，效果不是很好。
     */
    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (isPressed()) {
            if (mSource == SOURCE_HOMEWORK_TROOP) {
                super.setColorFilter(0x1A000000);
            } else if (Build.VERSION.SDK_INT >= 16) {
                super.setImageAlpha((int) (mToAlpha * 0xFF));
            } else if (Build.VERSION.SDK_INT >= 11) {
                super.setAlpha(mToAlpha);
            } else {
                super.setColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
            }
        } else {
            resetUnPressState();
        }
    }

}
