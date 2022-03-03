package com.demo.ipc;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class SubPreLoadService extends Service {

    private static final String TAG = "SubPreLoadService";

    public SubPreLoadService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "[onCreate]");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "[onStartCommand]");

//        new Handler().postDelayed(ProcessUtil::killMyProcess, 5000);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "[onBind]");
        return null;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "[onDestroy]");
        super.onDestroy();
    }
}