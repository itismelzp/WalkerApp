package com.demo.customview.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.demo.customview.R;

public class QzoneDualWarmActivity extends Activity implements View.OnClickListener {

    public static final String TAG = "QzoneDualWarmActivity";

    private LinearLayout mRootView;
    private LinearLayout mCommentsContainer;
    private LinearLayout mShuffleBtn;
    private View mPanel;

    private int mRankCommentPos = 1; // 智能推荐评论的位置 (默认为第二条)
    private int mRankCommentPullNum = RANK_COMMENT_PULL_NUM_DEFAULT; // 拉取的智能推荐评论的数量
    private int mCurrRankCommentIndex = 0; // 当前智能推荐评论的下标

    private static final int RANK_COMMENT_PULL_NUM_DEFAULT = 1; // 拉取智能推荐评论数量的默认值

//    @Override
//    public void setTheme(int resid) {
//        super.setTheme(R.style.TranslucentTheme);
//    }

    static int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setTheme(R.style.TranslucentTheme);
        getTheme().applyStyle(R.style.TranslucentTheme, true);

        setContentView(R.layout.qzone_dual_warm_panel);

//

        i++;
        int resId =0;
        if (i % 3 == 0) {
            getWindow().setBackgroundDrawableResource(R.color.black);
        } else if (i % 3 == 1) {
            getWindow().setBackgroundDrawableResource(R.color.transparent);
        }
        Log.e(TAG, "i==" + i);

//        if (i % 2 == 0) {
//            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
//                    WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        }

//        mCommentsContainer = (LinearLayout) findViewById(R.id.qz_dual_warm_container);
//        mRootView = (LinearLayout) findViewById(R.id.qz_dual_warm_panel_root);
//        mShuffleBtn = (LinearLayout) findViewById(R.id.qz_dual_warm_shuffle_btn);
//        mPanel = findViewById(R.id.qz_dual_warm_whole_container);

//        mRootView.setOnClickListener(this);
//        mShuffleBtn.setOnClickListener(this);


    }


    @Override
    protected void onResume() {
        super.onResume();
        getWindow().addFlags(0x04000000);
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);  //去除左右滑出动画 注意加载的时机，即Activity#onCreate之前调用   Activity#finish之后调用
    }


    @Override
    public void onClick(View v) {

    }
}
