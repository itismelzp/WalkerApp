package com.demo;

import android.app.Activity;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;

import com.demo.base.BaseActivity;
import com.demo.databinding.ActivityMainBinding;
import com.demo.fragment.GridFragment;
import com.demo.fragment.MainFragment;
import com.demo.base.log.MyLog;


public class MainActivity extends BaseActivity<ActivityMainBinding> implements GridFragment.OnActionListener {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        StatusBarUtil.transparencyBar(this);

        MyLog.i(TAG, "[onCreate]");
        if (savedInstanceState == null) { // 防止屏幕旋转时多次创建页面
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container,
                            MainFragment.newInstance("hello world", "hello main fragment"),
                            MainFragment.class.getSimpleName())
                    .commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void startActivity(Activity host, int resId, Class<?> clazz) {
        startActivity(host, resId, clazz, -1);
    }

    private void startActivity(Activity host, int resId, Class<?> clazz, int flag) {
        host.findViewById(resId).setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setClass(host, clazz);
            if (flag != -1) {
                intent.setFlags(flag);
            }
            intent.putExtra("startTime", System.currentTimeMillis());
            host.startActivity(intent);
        });
    }

    @Override
    public void onAction(@NonNull String msg) {
        toast(TAG + "：" + msg);
    }

}
