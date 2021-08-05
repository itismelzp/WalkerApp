package com.demo.customview.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import com.demo.R;
import com.demo.customview.drawable.RoundImageDrawable;

public class CustomShaderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_drawable);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.qcircle_danmu_bg);
        ImageView iv = findViewById(R.id.custom_drawable);
        iv.setBackgroundDrawable(new RoundImageDrawable(bitmap));
    }
}
