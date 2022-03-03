package com.tencent.wink.storage.winkkv.multiprocess;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;

import com.tencent.wink.storage.winkkv.WinkKV;
import com.tencent.wink.storage.winkkv.WinkKVUtil;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class WinkKVService extends Service {

    private static final String TAG = "WinkKVService";

    public static final int MY_TRANSACT_CODE = 920511;

    private IBinder mBinder;

    public WinkKVService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        if (mBinder == null) {
            mBinder = new MyBinder(WinkKVUtil.getWinkKV(this));
        }
        return mBinder;
    }

    private static class MyBinder extends IAshmemAidlInterface.Stub {

        private final WinkKV mWinkKV;
        private String acceptData;

        public MyBinder(WinkKV winkKV) {
            this.mWinkKV = winkKV;
        }

        @Override
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            boolean parent = super.onTransact(code, data, reply, flags);
            if (code != MY_TRANSACT_CODE && code != 931114) {
                return parent;
            }

            // accept data
            ParcelFileDescriptor pfd = data.readParcelable(getClass().getClassLoader());
            if (pfd == null) {
                return parent;
            }
            FileDescriptor fd = pfd.getFileDescriptor();
            try {

                // 读取顺序要与写入顺序一致
                int size = data.readInt();
                Bundle bundle = data.readBundle(getClass().getClassLoader());

                FileInputStream fis = new FileInputStream(fd);
                byte[] bytes = new byte[size];
                fis.read(bytes);
                acceptData = new String(bytes, 0, size, StandardCharsets.UTF_8);

                // send data
                if (reply != null) {
                    reply.writeString("Server端收到FileDescriptor, 并且从共享内存中读到了数据。");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return true;
        }

        @Override
        public void putAll(Map map) throws RemoteException {
            mWinkKV.putAll(map);
        }

        @Override
        public Map<String, Object> getAll() throws RemoteException {
            return mWinkKV.getAll();
        }

        @Override
        public void putString(String key, String value) throws RemoteException {
            mWinkKV.putString(key, value);
        }

        @Override
        public String getString(String key) throws RemoteException {
            return mWinkKV.getString(key);
        }

        @Override
        public void putBoolean(String key, boolean value) throws RemoteException {
            mWinkKV.putBoolean(key, value);
        }

        @Override
        public boolean getBoolean(String key) throws RemoteException {
            return mWinkKV.getBoolean(key);
        }

        @Override
        public void putInt(String key, int value) throws RemoteException {
            mWinkKV.putInt(key, value);
        }

        @Override
        public int getInt(String key) throws RemoteException {
            return mWinkKV.getInt(key);
        }

        @Override
        public void putFloat(String key, float value) throws RemoteException {
            mWinkKV.putFloat(key, value);
        }

        @Override
        public float getFloat(String key) throws RemoteException {
            return mWinkKV.getFloat(key);
        }

        @Override
        public void putDouble(String key, double value) throws RemoteException {
            mWinkKV.putDouble(key, value);
        }

        @Override
        public double getDouble(String key) throws RemoteException {
            return mWinkKV.getDouble(key);
        }
    }
}