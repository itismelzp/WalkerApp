package com.demo.customview.ryg;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.demo.R;
import com.demo.customview.utils.ViewUtils;

import java.util.Locale;

public class ViewEventDispatchDemoActivity extends AppCompatActivity {

    private static final String TAG = "ViewEventDispatchDemo";

    private static final int TOUCH_SLOP = ViewUtils.getTouchSlop();

    private DispatchView mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event_dispatch_demo);

        mView = findViewById(R.id.test_id);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mView.post(new Runnable() {
            @Override
            public void run() {
                String log = String.format(Locale.getDefault(),
                        "getDensity: %f, getX: %f, getY: %f " +
                                "left: %d, top: %d, right: %d, bottom: %d, width: %d, height: %d",
                        ViewUtils.getDensity(),
                        mView.getX(), mView.getY(),
                        mView.getLeft(), mView.getTop(), mView.getRight(), mView.getBottom(),
                        mView.getWidth(), ViewUtils.pxTosp(mView.getHeight())
                );
                Log.d(TAG, log);
            }
        });
    }
}