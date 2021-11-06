package com.demo.customview.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.demo.R;

public class CustomMatrixActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "CustomMatrixActivity";

    private Button button;
    private View bannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_matrix);
        button = findViewById(R.id.button);
        bannerView = findViewById(R.id.album_banner_view);
        button.setOnClickListener(this);
        bannerView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button) {
            Log.e(TAG, "[onClick] button.");
        } else if(view.getId() == R.id.album_banner_view) {
            Log.e(TAG, "[onClick] album_banner_view.");
        }
    }
}
