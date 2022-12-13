package com.demo.apt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.demo.R;
import com.tencent.wink.apt.annotation.BindView;
import com.tencent.wink.apt.library.BindViewTools;

public class AptDemoActivity extends AppCompatActivity {

    private static final String TAG = "AptDemoActivity";

    @BindView(R.id.btn_apt_test)
    Button mButton;

    @BindView(R.id.btn_apt_test_2)
    Button mButton2;

    Button mBtnKaptTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apt_demo);

        BindViewTools.bind(this);

        if (mButton != null) {
            mButton.setOnClickListener(view ->
                    Toast.makeText(AptDemoActivity.this, "bind view success..", Toast.LENGTH_SHORT).show()
            );
        }
        if (mButton2 != null) {
            mButton2.setOnClickListener(view ->
                    Toast.makeText(AptDemoActivity.this, "bind mButton2 success..", Toast.LENGTH_SHORT).show()
            );
        }
        if (mBtnKaptTest != null) {
            mBtnKaptTest.setOnClickListener(view ->
                    Toast.makeText(AptDemoActivity.this, "bind mBtnKaptTest success..", Toast.LENGTH_SHORT).show()
            );
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}