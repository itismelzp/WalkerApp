package com.demo;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.demo.apt.AptDemoActivity;
import com.demo.customview.activity.CustomMatrixActivity;
import com.demo.customview.activity.CustomShaderActivity;
import com.demo.customview.activity.ListViewDemoActivity;
import com.demo.customview.activity.OtherProcessActivity;
import com.demo.customview.aige.activity.AigeActivity;
import com.demo.customview.ryg.ViewEventDispatchDemoActivity;
import com.demo.customview.sloop.activity.CustomSloopMenuActivity;
import com.demo.customview.zhy.activity.CustomViewActivity;
import com.demo.storage.RoomActivity;
import com.demo.widget.activity.ScaleActivity;
import com.demo.widget.activity.ShapeBgActivity;
import com.demo.wink.WinkActivity;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        initButton(R.id.btn_custom_view, CustomViewActivity.class);
        initButton(R.id.btn_custom_drawable, CustomShaderActivity.class);
        initButton(R.id.btn_custom_sloop_menu, CustomSloopMenuActivity.class);
        initButton(R.id.btn_custom_matrix_view, CustomMatrixActivity.class);
        initButton(R.id.btn_aige_custom_view, AigeActivity.class);
        initButton(R.id.btn_list_view_demo, ListViewDemoActivity.class);
        initButton(R.id.test_other_process, OtherProcessActivity.class);
        initButton(R.id.btn_test_recycle_view, ScaleActivity.class);
        initButton(R.id.shape_bg, ShapeBgActivity.class);
        initButton(R.id.wink_pg, WinkActivity.class);
        initButton(R.id.btn_storage, RoomActivity.class);
        initButton(R.id.view_dispatch_demo, ViewEventDispatchDemoActivity.class);
        initButton(R.id.test_apt_demo, AptDemoActivity.class);

        Log.d(TAG, "onCreate currentThread: " + Thread.currentThread().getName());

    }

    private void initButton(int resId, final Class<?> clzz) {

        findViewById(resId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(clzz);
            }
        });

    }

    private void startActivity(Class<?> clzz) {
        Intent intent = new Intent(MainActivity.this, clzz);
        intent.putExtra("TAG", "MainActivity");
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
