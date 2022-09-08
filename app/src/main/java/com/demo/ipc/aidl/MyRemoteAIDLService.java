package com.demo.ipc.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 带IPC回调的Service，可实现Service与Client的双向通信
 * <p>
 * Created by walkerzpli on 2022/8/20.
 */
public class MyRemoteAIDLService extends Service {

    private static HashMap<String, IRemoteCallback> mHashMap;
    private static Handler sendHandler;
    private static Handler receiveHandler;

    private IBinder mIBinder;

    public MyRemoteAIDLService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mHashMap = new HashMap<>();

        initSendHandler();
        initReceiveHandler();
    }

    @Override
    public IBinder onBind(Intent intent) {
        if (mIBinder == null) {
            mIBinder = new MyRemoteBinder();
        }
        return mIBinder;
    }

    private void initReceiveHandler() {
        HandlerThread receiveThread = new HandlerThread("receive-thread");
        receiveThread.start();
        receiveHandler = new Handler(receiveThread.getLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                Bundle data = message.getData();
                String packageName = data.getString("packageName", "");
                String func = data.getString("func", "");
                String params = data.getString("params", "");
                IRemoteCallback iRemoteCallback = mHashMap.get(packageName);
                if (null != iRemoteCallback) {
                    try {
                        iRemoteCallback.onReceiver(func, 0, "收到请求啦，给你返回来的数据请接收");
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                return true;
            }
        });
    }

    private void initSendHandler() {
        HandlerThread sendThread = new HandlerThread("send-thread");
        sendThread.start();
        sendHandler = new Handler(sendThread.getLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                Bundle data = message.getData();
                String packageName = data.getString("packageName", "");
                String func = data.getString("func", "");
                String params = data.getString("params", "");
                if (TextUtils.isEmpty(func)) {
                    return true;
                }

                if (!TextUtils.isEmpty(packageName)) {
                    if (mHashMap.containsKey(packageName)) {
                        IRemoteCallback iRemoteCallback = mHashMap.get(packageName);
                        if (null == iRemoteCallback) {
                            return true;
                        }
                        try {
                            iRemoteCallback.onReceiver(func, 0, params);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    Set<Map.Entry<String, IRemoteCallback>> entries = mHashMap.entrySet();
                    for (Map.Entry<String, IRemoteCallback> entry : entries) {
                        try {
                            entry.getValue().onReceiver(func, 0, params);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                }

                return true;
            }
        });
    }

    static class MyRemoteBinder extends IRemoteService.Stub {

        @Override
        public void register(String pkgName, IRemoteCallback callback) throws RemoteException {
            if (callback == null) {
                return;
            }
            if (mHashMap.containsKey(pkgName)) {
                return;
            }

            callback.asBinder().linkToDeath(new IBinder.DeathRecipient() {
                @Override
                public void binderDied() {
                    IRemoteCallback remoteCallback = mHashMap.get(pkgName);
                    if (remoteCallback != null) {
                        remoteCallback.asBinder().unlinkToDeath(this, 0);
                    }
                    mHashMap.remove(pkgName);
                }
            }, 0);
            mHashMap.put(pkgName, callback);
        }

        @Override
        public void unRegister(String pkgName, IRemoteCallback callback) throws RemoteException {
            if (callback == null) {
                return;
            }
            mHashMap.remove(pkgName);
        }

        @Override
        public void send(String packageName, String func, String params) throws RemoteException {
            Message message = receiveHandler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putString("packageName", packageName);
            bundle.putString("func", func);
            bundle.putString("params", params);
            message.setData(bundle);
            receiveHandler.sendMessage(message);
        }

        @Override
        public String fetch(String packageName, String func) throws RemoteException {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("func", func);
                jsonObject.put("result", "主动获取的结果：" + System.currentTimeMillis());
                return jsonObject.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


}