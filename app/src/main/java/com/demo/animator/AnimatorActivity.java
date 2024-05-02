package com.demo.animator;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.demo.R;

public class AnimatorActivity extends AppCompatActivity {

    private static final String TAG = "AnimatorActivity";

    private TextSwitcher txtSwitcher;

    private String[] items;
    private int i = 0;

    private static final int RED = 0xffFF8080;
    private static final int BLUE = 0xff8080FF;
    private static final int CYAN = 0xff80ffff;
    private static final int GREEN = 0xff80ff80;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animator);

        initFloatTitleUI();
        initSwitcherUI();

        BorderGlowView borderGlowView = findViewById(R.id.border_glow_view);
        borderGlowView.startGlowAnimation();
    }

    private void initFloatTitleUI() {
        LinearLayout floatRankLL = findViewById(R.id.qfs_tag_challenge_float_rank_bg);

        ValueAnimator animator = ValueAnimator.ofInt(100, 0);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int currentValue = (int) animation.getAnimatedValue();
                Log.d(TAG, "currentValue: " + currentValue);
                if (currentValue < 20) {
                    Drawable drawable = floatRankLL.getResources()
                            .getDrawable(R.drawable.qcircle_tag_challenge_float_desc_bg_shape,
                                    null);
                    int alpha = 255 * currentValue / 20;
                    Log.d(TAG, "alpha: " + alpha);
                    drawable.setAlpha(alpha);
                    floatRankLL.setBackground(drawable);
                }
            }
        });
        animator.setDuration(1000L);
        animator.start();
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            txtSwitcher.setText(items[i % items.length]);
            i++;
            Message msgg = mHandler.obtainMessage(1);
            msgg.what = i;
            mHandler.sendMessageDelayed(msgg, 3000);
        }
    };


    private void initSwitcherUI() {

        // 设置标题
        // 控件
        txtSwitcher = (TextSwitcher) findViewById(R.id.textswitcher);
        txtSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView tv = new TextView(getApplication());
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                tv.setTextColor(0xFFFF0000);
                return tv;
            }
        });
        txtSwitcher.setInAnimation(getApplicationContext(), R.anim.slide_in_bottom);
        txtSwitcher.setOutAnimation(getApplicationContext(), R.anim.slide_out_top);
        txtSwitcher.setText("xxxxx");
        items = new String[] { "新春特别活动，楚舆狂歌套限时出售！", "三周年红发效果图放出！", "冬至趣味活动开启，一起来吃冬至宴席。" };
        Message msg = mHandler.obtainMessage(1);
        msg.what = i;
        mHandler.sendMessage(msg);
    }
}