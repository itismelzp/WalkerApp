package com.demo.ipc;


import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class SubPreLoadService extends IntentService {

    private static final String TAG = "SubPreLoadService";

    public SubPreLoadService() {
        super("SubPreLoadService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "[onCreate]");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "[onBind]");
        return null;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String value = null;
        if (intent != null) {
            value = intent.getStringExtra("walker");
        }
        Log.i(TAG, "[onHandleIntent] value: " + value + ",cur: " + Thread.currentThread());
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "[onDestroy]");
        super.onDestroy();
    }
}