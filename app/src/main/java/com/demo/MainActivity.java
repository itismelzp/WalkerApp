package com.demo;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.demo.animator.AnimatorActivity;
import com.demo.apt.AptDemoActivity;
import com.demo.customview.activity.CustomMatrixActivity;
import com.demo.customview.activity.CustomShaderActivity;
import com.demo.customview.activity.ListViewDemoActivity;
import com.demo.customview.activity.OtherProcessActivity;
import com.demo.customview.activity.SlideConflictDemoActivity;
import com.demo.customview.aige.activity.AigeActivity;
import com.demo.customview.ryg.ViewEventDispatchDemoActivity;
import com.demo.customview.sloop.activity.CustomSloopMenuActivity;
import com.demo.customview.zhy.activity.CustomViewActivity;
import com.demo.rxjava.RxJavaActivity;
import com.demo.storage.RoomActivity;
import com.demo.storage.WinkKVDemoActivity;
import com.demo.widget.activity.ScaleActivity;
import com.demo.widget.activity.ShapeBgActivity;
import com.demo.wink.WinkActivity;
import com.walker.apt.annotation.BindButton;
import com.walker.apt.library.BindButtonTools;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @BindButton(resId = R.id.btn_custom_view, clazz = CustomViewActivity.class)
    private Button customViewBtn;

    @BindButton(resId = R.id.btn_custom_drawable, clazz = CustomShaderActivity.class)
    private Button customDrawableBtn;

    @BindButton(resId = R.id.btn_custom_sloop_menu, clazz = CustomSloopMenuActivity.class)
    private Button customSloopMenuBtn;

    @BindButton(resId = R.id.btn_custom_matrix_view, clazz = CustomMatrixActivity.class)
    private Button customMatrixViewBtn;

    @BindButton(resId = R.id.btn_aige_custom_view, clazz = AigeActivity.class)
    private Button aigeCustomViewBtn;

    @BindButton(resId = R.id.btn_list_view_demo, clazz = ListViewDemoActivity.class)
    private Button listViewDemoBtn;

    @BindButton(resId = R.id.test_other_process, clazz = OtherProcessActivity.class)
    private Button otherProcessTest;

    @BindButton(resId = R.id.btn_test_recycle_view, clazz = ScaleActivity.class)
    private Button recycleViewTest;

    @BindButton(resId = R.id.shape_bg, clazz = ShapeBgActivity.class)
    private Button shapeBgBtn;

    @BindButton(resId = R.id.wink_pg, clazz = WinkActivity.class)
    private Button winkPgBtn;

    @BindButton(resId = R.id.btn_storage, clazz = RoomActivity.class)
    private Button storageBtn;

    @BindButton(resId = R.id.view_dispatch_demo, clazz = ViewEventDispatchDemoActivity.class)
    private Button dispatchView;

    @BindButton(resId = R.id.test_apt_demo, clazz = AptDemoActivity.class)
    private Button testAptDemoBtn;

    @BindButton(resId = R.id.slide_conflict_demo, clazz = SlideConflictDemoActivity.class)
    private Button slideConflictBtn;


    @BindButton(resId = R.id.wink_kv_demo, clazz = WinkKVDemoActivity.class)
    private Button winkKvBtn;

    @BindButton(resId = R.id.animator_test, clazz = AnimatorActivity.class)
    private Button animatorTestBtn;

    @BindButton(resId = R.id.rxjava_test, clazz = RxJavaActivity.class)
    private Button rxjavaTestBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BindButtonTools.bind(this);
//        MainActivity$ViewBinding binding = new MainActivity$ViewBinding();
//        binding.bind(this);

        Log.d(TAG, "onCreate currentThread: " + Thread.currentThread().getName());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
