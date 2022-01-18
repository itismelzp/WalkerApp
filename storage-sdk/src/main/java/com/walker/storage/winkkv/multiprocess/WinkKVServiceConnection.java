package com.walker.storage.winkkv.multiprocess;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;

import com.walker.storage.winkkv.log.WinkKVLog;


/**
 * 自定义ServiceConnection类
 * <p>
 * Created by walkerzpli on 2022/1/8.
 */
public class WinkKVServiceConnection implements ServiceConnection {

    private static final String TAG = "WinkKVServiceConnection";

    private final byte[] bytes;

    public WinkKVServiceConnection(byte[] bytes) {
        this.bytes = bytes;
    }


    @Override
    public void onServiceConnected(ComponentName componentName, IBinder binder) {
        if (binder == null) {
            WinkKVLog.e(TAG, "binder == null");
            return;
        }

        if (bytes == null || bytes.length == 0) {
            WinkKVLog.e(TAG, "bytes == null || bytes.length == 0");
            return;
        }

        // 创建 MemoryFile，并拿到 ParcelFileDescriptor
        ParcelFileDescriptor descriptor = WinkAshmemUtil.createMemoryFile(bytes);
        if (descriptor == null) {
            WinkKVLog.e(TAG, "descriptor == nul");
            return;
        }

        // 传递 FileDescriptor 和 共享内存中数据的大小
        Parcel sendData = Parcel.obtain();
        sendData.writeParcelable(descriptor, 0);

        sendData.writeInt(bytes.length);
        // 保存对方进程的返回值
        Parcel reply = Parcel.obtain();

        // 开始跨进程传递
        try {
            binder.transact(WinkAshmemUtil.TRANSACT_CODE, sendData, reply, 0);
        } catch (RemoteException e) {
            e.printStackTrace();
            WinkKVLog.e(TAG, "transact failed.", e);
        }

        // 读取 Binder 执行的结果
        String msg = reply.readString();
        WinkKVLog.i(TAG, "Binder 执行结果是：" + msg);
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }

}
