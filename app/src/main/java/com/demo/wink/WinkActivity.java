package com.demo.wink;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.widget.TextView;

import com.demo.R;

public class WinkActivity extends AppCompatActivity {


    private QFSRoundProgressView cornerView;
    private TextView centerView;

    private int progress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wink);

        cornerView = findViewById(R.id.progress_cv);
        centerView = findViewById(R.id.guild_channel_detail_title_tv);

        cornerView.setOnClickListener(v -> {
            progress = (progress + 2) % 100;
            cornerView.setProgress(progress);
            cornerView.setImageDrawable(ContextCompat.getDrawable(WinkActivity.this, R.drawable.wid_grid_bg));
        });

        centerView.setOnClickListener(v-> centerView.setText(centerView.getText() + ",王者荣耀营地"));

        cornerView.postDelayed(() -> cornerView.setProgressRange(0, 50), 1000);

    }
}