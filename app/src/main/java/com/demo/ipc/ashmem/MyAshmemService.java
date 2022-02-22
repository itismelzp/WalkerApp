package com.demo.ipc.ashmem;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.util.Log;

import com.demo.ipc.ProcessUtil;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;

public class MyAshmemService extends Service {

    private static final String TAG = "MyAshmemService";

    public static final int MY_TRANSACT_CODE = 920511;
    private MyBinder mMyBinder;

    public MyAshmemService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        if (mMyBinder == null) {
            mMyBinder = new MyBinder();
        }
        return mMyBinder;
    }

    private static class MyBinder extends IMyAshmemAidlInterface.Stub {

        private String acceptData;

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
                Log.i(TAG, "[Ashmem][onTransact] " + ProcessUtil.getCurProcessLog() + ", int_key: " + bundle.getInt("int_key"));
                Log.i(TAG, "[Ashmem][onTransact] " + ProcessUtil.getCurProcessLog() + ", boolean_key: " + bundle.getBoolean("boolean_key"));
                Log.i(TAG, "[Ashmem][onTransact] " + ProcessUtil.getCurProcessLog() + ", str_key: " + bundle.getString("str_key"));

                FileInputStream fis = new FileInputStream(fd);
                byte[] bytes = new byte[size];
                fis.read(bytes);
                acceptData = new String(bytes, 0, size, StandardCharsets.UTF_8);
                Log.i(TAG, "[Ashmem][onTransact] " + ProcessUtil.getCurProcessLog() + ", read data from other process：" + acceptData);

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
        public String getString() throws RemoteException {
            return acceptData;
        }
    }
}