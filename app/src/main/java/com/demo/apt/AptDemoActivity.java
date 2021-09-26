package com.demo.apt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.demo.R;
import com.walker.apt.annotation.BindView;
import com.walker.apt.library.BindViewTools;

public class AptDemoActivity extends AppCompatActivity {

    private static final String TAG = "AptDemoActivity";

    @BindView(R.id.btn_apt_test)
    Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apt_demo);

        BindViewTools.bind(this);

        mButton.setOnClickListener(view ->
                Toast.makeText(AptDemoActivity.this, "bind view success..", Toast.LENGTH_SHORT).show()
        );

    }
}