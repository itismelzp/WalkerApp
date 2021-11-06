package com.demo.customview.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.demo.R;
import com.demo.customview.slide_conflict.SeekFrameLayout;
import com.demo.customview.utils.ViewUtils;

public class SlideConflictDemoActivity extends AppCompatActivity {

    private static final String TAG = "SlideConflictDemo";

    private SeekFrameLayout mSeekContainer;
    private FrameLayout mSeekFrameLayout;
    private SeekBar mSeekBar;// 播放器seekBar
    private View albumLL;

    private int mLastX = 0;
    private int mLastY = 0;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_conflict_demo);

        mSeekBar = findViewById(R.id.flash_show_progress_bar);
        albumLL = findViewById(R.id.qfs_feed_album_ll);
        mSeekFrameLayout = findViewById(R.id.flash_seek_bar_frame_layout);
        mSeekContainer = findViewById(R.id.seek_container);

        albumLL.setOnClickListener(listener);
        mSeekFrameLayout.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("LongLogTag")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int interceptType = 0;
                String action = "";
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        action = "ACTION_DOWN";
                        interceptType = 0;
                        mLastX = (int) event.getX();
                        break;
                    case MotionEvent.ACTION_MOVE: {
                        int deltaX = (int) event.getX() - mLastX;
                        if (Math.abs(deltaX) > ViewUtils.dip2px(10)) {
                            interceptType = 1;
                        } else {
                            interceptType = 0;
                        }
                        action = "ACTION_MOVE";
                        break;
                    }

                    case MotionEvent.ACTION_UP: {
                        int deltaX = (int) event.getX() - mLastX;
                        Log.d(TAG, "[onInterceptTouchEvent] deltaX: " + deltaX + ", 10dp: " + ViewUtils.dip2px(10));
                        if (Math.abs(deltaX) > ViewUtils.dip2px(10)) {
                            interceptType = 1;
                        } else {
                            interceptType = 2;
                        }
                        mLastX = 0;
                        action = "ACTION_UP";
                        break;
                    }
                }

                mSeekFrameLayout.requestDisallowInterceptTouchEvent(true);
                if (interceptType == 1) {
                    mSeekBar.onTouchEvent(event);
                } else if (interceptType == 2) {
                    if (albumLL.getVisibility() == View.VISIBLE) {
                        listener.onClick(albumLL);
                    }
                }
                Log.d(TAG, "[onInterceptTouchEvent] event.getAction(): " + action + ", interceptType: " + interceptType);
                return true;
            }
        });
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d(TAG, "albumLL onClick");
            Toast.makeText(SlideConflictDemoActivity.this, "albumLL onClick", Toast.LENGTH_SHORT).show();
        }
    };
}