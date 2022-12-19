package com.demo;

import android.app.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.demo.fragment.MainFragment;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container,
                        MainFragment.newInstance("hello world", "hello main fragment"), "mainFragment")
//                .addToBackStack("mainFragment")
                .commit();
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

}
