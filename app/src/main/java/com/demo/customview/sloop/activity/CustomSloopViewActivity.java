package com.demo.customview.sloop.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.demo.customview.R;

public class CustomSloopViewActivity extends AppCompatActivity {

    public static final String RES_ID = CustomSloopMenuActivity.RES_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int resId = getIntent().getIntExtra(RES_ID, -1);
        if (resId == R.id.btn_corner_pathEffect) {
            setContentView(R.layout.activity_custom_sloop_view);
        } else if (resId == R.id.btn_path_demo) {
            setContentView(R.layout.activity_custom_sloop_path_demo_view);
        }
    }
}
