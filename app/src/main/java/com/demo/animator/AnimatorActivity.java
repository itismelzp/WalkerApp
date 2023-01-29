package com.demo.animator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.demo.R;

public class AnimatorActivity extends AppCompatActivity {

    private static final String TAG = "AnimatorActivity";

    private TextSwitcher txtSwitcher;

    private static String[] items;
    private static int i = 0;

    private static final int RED = 0xFFFF8080;
    private static final int BLUE = 0xFF8080FF;
    private static final int CYAN = 0xFF80FFFF;
    private static final int GREEN = 0xFF80FF80;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animator);

//        initFloatTitleUI();
        initFloatTitleUI2();
        initSwitcherUI();
    }

    private void initFloatTitleUI() {
        LinearLayout floatRankLL = findViewById(R.id.qfs_tag_challenge_float_rank_bg);

        ValueAnimator animator = ValueAnimator.ofInt(100, 0);
        animator.addUpdateListener(animation -> {
            int currentValue = (int) animation.getAnimatedValue();
            Log.d(TAG, "currentValue: " + currentValue);
            if (currentValue < 20) {
                Drawable drawable = ResourcesCompat.getDrawable(getResources(),
                                R.drawable.qcircle_tag_challenge_float_desc_bg_shape,
                                null);
                int alpha = 255 * currentValue / 20;
                Log.d(TAG, "alpha: " + alpha);
                if (drawable != null) {
                    drawable.setAlpha(alpha);
                    floatRankLL.setBackground(drawable);
                }
            }
        });
        animator.setDuration(1000L);
        animator.start();
    }

    private void initFloatTitleUI2() {
        LinearLayout floatRankLL = findViewById(R.id.qfs_tag_challenge_float_rank_bg);

        Drawable drawable = ResourcesCompat.getDrawable(getResources(),
                R.drawable.qcircle_tag_challenge_float_desc_bg_shape,
                null);
        floatRankLL.setBackground(drawable);
        ObjectAnimator animator = ObjectAnimator.ofInt(drawable, "alpha", 255, 0);
        animator.setDuration(1000L);
        animator.start();

        AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(this,
                R.animator.property_animator);
        set.setTarget(floatRankLL);
        set.start();

    }

    static class InnerHandler extends Handler {

        private final TextSwitcher txtSwitcher;

        public InnerHandler(@NonNull Looper looper, TextSwitcher txtSwitcher) {
            super(looper);
            this.txtSwitcher = txtSwitcher;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            txtSwitcher.setText(items[i % items.length]);
            i++;
            Message newMsg = obtainMessage(1);
            newMsg.what = i;
            sendMessageDelayed(newMsg, 3000);
        }
    };


    private void initSwitcherUI() {

        // 设置标题
        // 控件
        txtSwitcher = (TextSwitcher) findViewById(R.id.textswitcher);
        txtSwitcher.setFactory(() -> {
            TextView tv = new TextView(getApplication());
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
            tv.setTextColor(0xFFFF0000);
            return tv;
        });
        txtSwitcher.setInAnimation(getApplicationContext(), R.anim.slide_in_bottom);
        txtSwitcher.setOutAnimation(getApplicationContext(), R.anim.slide_out_top);
        txtSwitcher.setText("xxxxx");
        items = new String[] { "新春特别活动，楚舆狂歌套限时出售！", "三周年红发效果图放出！", "冬至趣味活动开启，一起来吃冬至宴席。" };

        InnerHandler handler = new InnerHandler(Looper.myLooper(), txtSwitcher);
        Message msg = handler.obtainMessage(1);
        msg.what = i;
        handler.sendMessage(msg);
    }
}