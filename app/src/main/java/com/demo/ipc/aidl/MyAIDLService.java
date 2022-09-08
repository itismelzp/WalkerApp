package com.demo.ipc.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.demo.MyApplication;
import com.demo.ipc.ProcessUtil;

/**
 * MyAIDLService在主进程，其它进程可通过bindService的方式与MyAIDLService通信，达到跨进程通信的目标。
 * <p>
 * 其它进程可通过IMyAidlInterface.Stub中的函数，拿此Service所在进程的数据。
 * <p>
 * Created by walkerzpli on 2022/2/20.
 */
public class MyAIDLService extends Service {

    private static final String TAG = "MyAIDLService";

    private IBinder mIBinder;

    public MyAIDLService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        if (mIBinder == null) {
            mIBinder = new MyBinder();
        }
        return mIBinder;
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