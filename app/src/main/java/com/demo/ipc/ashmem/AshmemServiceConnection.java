package com.demo.ipc.ashmem;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.MemoryFile;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.util.Log;

import com.demo.ipc.ProcessUtil;

import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

/**
 * Service连接。相对于Service的，此连接在与Service相对的进程中被使用。
 * <p>
 * 如Service在主进程中，AshmemServiceConnection则在Sub进程。
 * <p>
 * Created by walkerzpli on 2022/2/22.
 */
public class AshmemServiceConnection implements ServiceConnection {

    private static final String TAG = "AshmemServiceConnection";

    private static final String GET_FILE_DESCRIPTOR = "getFileDescriptor";

    private IMyAshmemAidlInterface mIMyAshmemAidlInterface;

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        if (iBinder == null) {
            return;
        }
        mIMyAshmemAidlInterface = IMyAshmemAidlInterface.Stub.asInterface(iBinder);

        Log.i(TAG, "[Ashmem][onServiceConnected] " + ProcessUtil.getCurProcessLog());

        byte[] bytes = "我画兰江水悠悠，爱晚亭上枫叶愁。".getBytes(StandardCharsets.UTF_8);
        ParcelFileDescriptor pfd = createMemoryFile(bytes);
        if (pfd == null) {
            return;
        }
        // send data
        Parcel sendData = Parcel.obtain();
        sendData.writeParcelable(pfd, 0);
        sendData.writeInt(bytes.length);

        Bundle bundle = new Bundle();
        bundle.putInt("int_key", 1234);
        bundle.putBoolean("boolean_key", true);
        bundle.putString("str_key", "hello world");
        sendData.writeBundle(bundle);

        // accept data
        Parcel replyData = Parcel.obtain();
        try {
            iBinder.transact(MyAshmemService.MY_TRANSACT_CODE, sendData, replyData, 0);
            String replyStr = replyData.readString();
            Log.i(TAG, "[Ashmem][onServiceConnected] " + ProcessUtil.getCurProcessLog() + ", replyStr: " + replyStr);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    public IMyAshmemAidlInterface getIMyAshmemAidlInterface() {
        return mIMyAshmemAidlInterface;
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        Log.i(TAG, "[Ashmem][onServiceDisconnected] " + ProcessUtil.getCurProcessLog() + ", componentName: " + componentName);
    }

    private ParcelFileDescriptor createMemoryFile(byte[] bytes) {
        try {
            MemoryFile memoryFile = new MemoryFile("test_ashmem_file", 1024);
            FileDescriptor fd = getFileDescriptor(memoryFile);

            if (fd == null) {
                return null;
            }
            memoryFile.writeBytes(bytes, 0, 0, bytes.length);
            return ParcelFileDescriptor.dup(fd);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private FileDescriptor getFileDescriptor(Object obj) {
        try {
            Method method = obj.getClass().getDeclaredMethod(GET_FILE_DESCRIPTOR);
            return (FileDescriptor) method.invoke(obj);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
