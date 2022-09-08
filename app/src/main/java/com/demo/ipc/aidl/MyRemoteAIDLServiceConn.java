package com.demo.ipc.aidl;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

public class MyRemoteAIDLServiceConn implements ServiceConnection {

    private IRemoteService iRemoteService;
    private IRemoteCallback iRemoteCallback;

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        iRemoteService = IRemoteService.Stub.asInterface(service);
        try {
            // 设置死亡代理，服务意外挂掉才能回调onServiceDisconnected
            iRemoteService.asBinder().linkToDeath(() -> {
            }, 0);
            iRemoteService.register("com.demo.ipc.aidl", iRemoteCallback);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        if (null != iRemoteService) {
            iRemoteService.asBinder().unlinkToDeath(() -> {

            }, 0);
        }
        iRemoteService = null;
        iRemoteCallback = null;
    }
}
