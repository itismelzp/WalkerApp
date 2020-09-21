package com.demo.customview.sloop.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.demo.customview.R;

public class CustomSloopMenuActivity extends AppCompatActivity {

    public static final String RES_ID = "RES_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_sloop_manu);

        initButton(R.id.btn_corner_pathEffect);
        initButton(R.id.btn_path_demo);
    }

    private void initButton(final int resId) {
        findViewById(resId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(resId);
            }
        });
    }

    private void startActivity(int resId) {
        Intent intent = new Intent(CustomSloopMenuActivity.this, CustomSloopViewActivity.class);
        intent.putExtra(RES_ID, resId);
        startActivity(intent);
    }

}
