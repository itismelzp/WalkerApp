package com.demo;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Looper;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Choreographer;

import com.tencent.wink.apt.library.BindButtonTools;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private long mLastFrameNanos;
    private static final long NANO_UNIT = 1_000_000L;

    private RecyclerView recyclerView;
    private MainListAdapter mainListAdapter;
    private MainButtonViewModel mainButtonViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BindButtonTools.bind(this);
//        MainActivity$ViewBinding binding = new MainActivity$ViewBinding();
//        binding.bind(this);
        initViewModel();

        Log.d(TAG, "onCreate currentThread: " + Thread.currentThread().getName());

        recyclerView = findViewById(R.id.main_rv);
        mainListAdapter = new MainListAdapter(new MainListAdapter.MainDiff());
        recyclerView.setAdapter(mainListAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

//        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.addItemDecoration(new MainListAdapter.SpaceItemDecoration());

        preLoadSub(this);

//        initMonitor(); // 卡顿监控

        MainButtonModel.initData(mainButtonViewModel);
    }

    private void initViewModel() {
        mainButtonViewModel = new ViewModelProvider(this).get(MainButtonViewModel.class);
        mainButtonViewModel.getMainButtonList().observe(this, mainButtons -> {
            mainListAdapter.submitList(mainButtons);
        });
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
