package com.tencent.wink.storage.winkkv.multiprocess;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.MemoryFile;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.util.Log;

import com.tencent.wink.storage.winkkv.ProcessUtil;
import com.tencent.wink.storage.winkkv.log.WinkKVLog;

import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


/**
 * 自定义ServiceConnection类
 * <p>
 * 如Service在主进程中，WinkKVServiceConnection则在Sub进程。
 * <p>
 * Created by walkerzpli on 2022/1/8.
 */
public class WinkKVServiceConnection implements ServiceConnection {

    private static final String TAG = "WinkKVServiceConnection";
    private volatile static WinkKVServiceConnection INSTANCE;

    private static final String GET_FILE_DESCRIPTOR = "getFileDescriptor";

    private IAshmemAidlInterface mIMyAshmemAidlInterface;
    private Parcel mReplyData;

    private WinkKVServiceConnection() {
    }

    public static WinkKVServiceConnection getInstance() {
        if (INSTANCE == null) {
            synchronized (WinkKVServiceConnection.class) {
                if (INSTANCE == null) {
                    INSTANCE = new WinkKVServiceConnection();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        if (iBinder == null) {
            return;
        }
        mIMyAshmemAidlInterface = IAshmemAidlInterface.Stub.asInterface(iBinder);
//            mLock.notify();
        Log.i(TAG, "[Ashmem][onServiceConnected] " + ProcessUtil.getCurProcessLog());

        ParcelFileDescriptor pfd = createMemoryFile();
        if (pfd == null) {
            return;
        }

        // accept data
        if (mReplyData == null) {
            mReplyData = Parcel.obtain();
        }
        String replyStr = mReplyData.readString();
        Bundle bundle = mReplyData.readBundle(getClass().getClassLoader());
        Log.i(TAG, "[Ashmem][onServiceConnected] " + ProcessUtil.getCurProcessLog() + ", bundle: " + bundle);
        Log.i(TAG, "[Ashmem][onServiceConnected] " + ProcessUtil.getCurProcessLog() + ", replyStr: " + replyStr);
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        if (mReplyData != null) {
            mReplyData.recycle();
        }
    }

    // WinkKV opt
    void putAll(Map<String, Object> dataMap) {
        checkIsWinkKVInitialized();
        try {
            mIMyAshmemAidlInterface.putAll(dataMap);
        } catch (RemoteException e) {
            WinkKVLog.d(TAG, "[putAll] error: " + e);
        }
    }

    Map<String, Object> getAll() {
        checkIsWinkKVInitialized();
        try {
            return mIMyAshmemAidlInterface.getAll();
        } catch (RemoteException e) {
            WinkKVLog.d(TAG, "[getAll] error: " + e);
        }
        return new HashMap<>();
    }

    void putString(String key, String value) {
        checkIsWinkKVInitialized();
        try {
            mIMyAshmemAidlInterface.putString(key, value);
        } catch (RemoteException e) {
            WinkKVLog.d(TAG, "[putString] error: " + e);
        }
    }

    String getString(String key) {
        checkIsWinkKVInitialized();
        try {
            return mIMyAshmemAidlInterface.getString(key);
        } catch (RemoteException e) {
            WinkKVLog.d(TAG, "[getString] error: " + e);
        }
        return "";
    }

    void putBoolean(String key, boolean value) {
        checkIsWinkKVInitialized();
        try {
            mIMyAshmemAidlInterface.putBoolean(key, value);
        } catch (RemoteException e) {
            WinkKVLog.d(TAG, "[putBoolean] error: " + e);
        }
    }

    boolean getBoolean(String key) {
        checkIsWinkKVInitialized();
        try {
            return mIMyAshmemAidlInterface.getBoolean(key);
        } catch (RemoteException e) {
            WinkKVLog.d(TAG, "[getBoolean] error: " + e);
        }
        return false;
    }

    void putInt(String key, int value) {
        checkIsWinkKVInitialized();
        try {
            mIMyAshmemAidlInterface.putInt(key, value);
        } catch (RemoteException e) {
            WinkKVLog.d(TAG, "[putInt] error: " + e);
        }
    }

    int getInt(String key) {
        checkIsWinkKVInitialized();
        try {
            return mIMyAshmemAidlInterface.getInt(key);
        } catch (RemoteException e) {
            WinkKVLog.d(TAG, "[getInt] error: " + e);
        }
        return 0;
    }

    void putFloat(String key, float value) {
        checkIsWinkKVInitialized();
        try {
            mIMyAshmemAidlInterface.putFloat(key, value);
        } catch (RemoteException e) {
            WinkKVLog.d(TAG, "[putFloat] error: " + e);
        }
    }

    float getFloat(String key) {
        checkIsWinkKVInitialized();
        try {
            return mIMyAshmemAidlInterface.getFloat(key);
        } catch (RemoteException e) {
            WinkKVLog.d(TAG, "[getFloat] error: " + e);
        }
        return 0L;
    }

    void putDouble(String key, double value) {
        checkIsWinkKVInitialized();
        try {
            mIMyAshmemAidlInterface.putDouble(key, value);
        } catch (RemoteException e) {
            WinkKVLog.d(TAG, "[putDouble] error: " + e);
        }
    }

    double getDouble(String key) {
        checkIsWinkKVInitialized();
        try {
            return mIMyAshmemAidlInterface.getDouble(key);
        } catch (RemoteException e) {
            WinkKVLog.d(TAG, "[getDouble] error: " + e);
        }
        return 0;
    }

    private boolean checkIsWinkKVInitialized() {
        if (mIMyAshmemAidlInterface == null) {
            throw new RuntimeException("isWinkKVInitialized haven't init, please init winkkv first.");
        }
        return true;
    }

    private ParcelFileDescriptor createMemoryFile() {
        try {
            MemoryFile memoryFile = new MemoryFile("winkkv_ashmem_file", 1024);
            FileDescriptor fd = getFileDescriptor(memoryFile);

            if (fd == null) {
                return null;
            }
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
