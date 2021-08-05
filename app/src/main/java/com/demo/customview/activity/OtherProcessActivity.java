package com.demo.customview.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.demo.R;

public class OtherProcessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_process);

        Intent intent = getIntent();
        if (intent != null) {
            String text = intent.getStringExtra("TAG");
            TextView textView = findViewById(R.id.text_other_process);
            textView.setText(text);
        }
    }
}