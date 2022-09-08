package com.demo.ipc.aidl;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;


/**
 * description
 * <p>
 * Created by walkerzpli on 2022/3/2.
 */
public class MyAIDLServiceConnection implements ServiceConnection {

    private static final String TAG = "MyAIDLServiceConnection";
    private IMyAidlInterface mIMyAidlInterface;
    private volatile boolean isConnect;

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        mIMyAidlInterface = IMyAidlInterface.Stub.asInterface(iBinder);
        Log.d(TAG, "[onServiceConnected] mIMyAidlInterface: " + mIMyAidlInterface);
        isConnect = true;
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        isConnect = false;
    }

    public String getName() {

        if (!isConnect) {
            Log.e(TAG, "[getName] ServiceConnection failed.");
            return null;
        }

        try {
            return mIMyAidlInterface.getName();
        } catch (RemoteException e) {
            e.printStackTrace();
            Log.e(TAG, "[getName] failed.", e);
        }
        return "";
    }
}
