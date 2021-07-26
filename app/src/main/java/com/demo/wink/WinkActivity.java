package com.demo.wink;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;

import com.demo.customview.R;

public class WinkActivity extends AppCompatActivity {


    private QFSRoundProgressView cornerView;

    private int progress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wink);

        cornerView = findViewById(R.id.progress_cv);

        cornerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress = (progress + 2) % 100;
                cornerView.setProgress(progress);
                cornerView.setImageDrawable(ContextCompat.getDrawable(WinkActivity.this, R.drawable.wid_grid_bg));
            }
        });

        cornerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                cornerView.setProgressRange(0, 50);
            }
        }, 1000);

    }
}