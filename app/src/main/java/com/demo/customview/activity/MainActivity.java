package com.demo.customview.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.demo.customview.R;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initButton(R.id.btn_custom_view, CustomViewActivity.class);
        initButton(R.id.btn_custom_drawable, CustomShaderActivity.class);

    }

    private void initButton(int redId, final Class clz) {
        findViewById(redId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(clz);
            }
        });
    }

    private void startActivity(Class clz) {
        Intent intent = new Intent(MainActivity.this, clz);
        startActivity(intent);
    }

}
