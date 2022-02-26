package com.demo.ipc.messenger;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Messenger;
import android.util.Log;

import com.demo.ipc.ProcessUtil;

/**
 * description
 * <p>
 * Created by walkerzpli on 2022/2/23.
 */
public class MessengerServiceConnection implements ServiceConnection {

    private static final String TAG = "MessengerServiceConnect";

    private boolean isConnect;
    private Messenger mService;

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        mService = new Messenger(iBinder);
        isConnect = true;
        Log.i(TAG, "[Messenger][onServiceConnected] " + ProcessUtil.getCurProcessLog());
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        mService = null;
        isConnect = false;
        Log.i(TAG, "[Messenger][onServiceDisconnected] " + ProcessUtil.getCurProcessLog());
    }

    public boolean isConnect() {
        return isConnect;
    }

    public Messenger getService() {
        return mService;
    }
}
