package com.demo.ipc.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.demo.MyApplication;
import com.demo.ipc.IMyAidlInterface;
import com.demo.ipc.ProcessUtil;

public class MyAIDLService extends Service {

    private static final String TAG = "MyService";

    public MyAIDLService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    static class MyBinder extends IMyAidlInterface.Stub {

        @Override
        public String getName() throws RemoteException {
            String name = "test";
            Log.i(TAG, "[getName] Process: "
                    + ProcessUtil.getCurrentProcessName(MyApplication.getInstance())
                    + ", send name: " + name
            );
            return name;
        }
    }
}