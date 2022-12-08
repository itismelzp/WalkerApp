package com.demo.apt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.walker.apt.Apt;
import com.walker.apt.Unbinder;
import com.walker.annotations.KBindView;
import com.demo.R;
import com.tencent.wink.apt.annotation.BindView;
import com.tencent.wink.apt.library.BindViewTools;

public class AptDemoActivity extends AppCompatActivity {

    private static final String TAG = "AptDemoActivity";

    @BindView(R.id.btn_apt_test)
    Button mButton;

    @BindView(R.id.btn_apt_test_2)
    Button mButton2;

    @KBindView(R.id.btn_kapt_test)
    Button mBtnKaptTest;

    private Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apt_demo);

        BindViewTools.bind(this);
        mUnbinder = Apt.bind(this);

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
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }
}