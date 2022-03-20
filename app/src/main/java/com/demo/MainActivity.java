package com.demo;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Looper;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Printer;
import android.view.Choreographer;
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
import com.demo.ipc.IPCDemoActivity;
import com.demo.rxjava.RxJavaActivity;
import com.demo.storage.RoomActivity;
import com.demo.storage.WinkKVDemoActivity;
import com.demo.widget.activity.ScaleActivity;
import com.demo.widget.activity.ShapeBgActivity;
import com.demo.wink.WinkActivity;
import com.tencent.wink.apt.annotation.BindButton;
import com.tencent.wink.apt.library.BindButtonTools;

import java.util.List;


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

    //    @BindButton(resId = R.id.test_other_process, clazz = OtherProcessActivity.class)
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


    @BindButton(resId = R.id.ipc_test, clazz = IPCDemoActivity.class)
    private Button ipcTest;

    private long mLastFrameNanos;
    private static final long NANO_UNIT = 1000000L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BindButtonTools.bind(this);
//        MainActivity$ViewBinding binding = new MainActivity$ViewBinding();
//        binding.bind(this);

        Log.d(TAG, "onCreate currentThread: " + Thread.currentThread().getName());

        startActivity(MainActivity.this, R.id.test_other_process, OtherProcessActivity.class);
        preLoadSub(this);

//        initMonitor(); // 卡顿监控
    }

    private void initMonitor() {

        // 1) Looper方案--BlockCanary
        Looper.getMainLooper().setMessageLogging(s -> {
            // >>>>> Dispatching to
            // <<<<< Finished to
            Log.d(TAG, "[println] s: " + s);
        });

        // 2) Choreographer方案--ArgusAPM、LogMonitor
        Choreographer.getInstance().postFrameCallback(new Choreographer.FrameCallback() {
            @Override
            public void doFrame(long frameTimeNanos) {
                if (mLastFrameNanos == 0L) {
                    mLastFrameNanos = frameTimeNanos;
                }
                if (frameTimeNanos - mLastFrameNanos > 100) {
                    //
                }
                Log.i(TAG, "[doFrame] time gap: " + (float) (frameTimeNanos - mLastFrameNanos) / NANO_UNIT + "ms");
                mLastFrameNanos = frameTimeNanos;
                Choreographer.getInstance().postFrameCallback(this);
            }
        });

        // 1) + 2)--Matrix
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
        Intent preLoader = new Intent();
        preLoader.setAction("com.demo.ipc.SubPreLoadService");
        preLoader.setPackage("com.demo");
        stopService(preLoader);
    }

    private void startActivity(Activity host, int resId, Class<?> clazz) {
        host.findViewById(resId).setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setClass(host, clazz);
            intent.putExtra("startTime", System.currentTimeMillis());
            host.startActivity(intent);
        });
    }


    private void preLoadSub(Context context) {
        if (context == null || isSubAlive(context)) {
            return;
        }
        new Thread(() -> {
            Intent preLoader = new Intent();
            preLoader.setAction("com.demo.ipc.SubPreLoadService");
            preLoader.setPackage("com.demo");
            try {
                context.startService(preLoader);
            } catch (Exception e) {
                Log.e(TAG, "[preLoadSub] preLoadSub failed.");
            }
            Log.e(TAG, "[preLoadSub] preLoadSub...");
        }).start();
    }

    private boolean isSubAlive(Context context) {
        try {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo info : processInfos) {
                String processName = info.processName;
                if ("com.demo:sub".equals(processName)) {
                    Log.e(TAG, "[isSubAlive] isSubAlive == true");
                    return true;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "get process info fail.");
        }
        Log.e(TAG, "[isSubAlive] isSubAlive == false");
        return false;
    }
}
