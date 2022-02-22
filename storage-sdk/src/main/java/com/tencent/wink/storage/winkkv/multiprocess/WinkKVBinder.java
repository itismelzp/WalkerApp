package com.tencent.wink.storage.winkkv.multiprocess;

import android.os.Binder;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tencent.wink.storage.winkkv.log.WinkKVLog;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * 数据传输Binder
 * <p>
 * Created by walkerzpli on 2022/1/8.
 */
public class WinkKVBinder extends Binder {

    private static final String TAG = "WinkKVBinder";

    private WinkKVService.CallbackOnBind callbackOnBind;

    public WinkKVBinder(WinkKVService.CallbackOnBind callbackOnBind) {
        this.callbackOnBind = callbackOnBind;
    }

    @Override
    protected boolean onTransact(int code, @NonNull Parcel data, @Nullable Parcel reply, int flags) throws RemoteException {
        boolean parent = super.onTransact(code, data, reply, flags);
        if (code != WinkAshmemUtil.TRANSACT_CODE && code != 931114) {
            return parent;
        }

        // 读取 ParcelFileDescriptor 并转为 FileDescriptor
        ParcelFileDescriptor pfd = data.readParcelable(this.getClass().getClassLoader());
        if (pfd == null) {
            return parent;
        }
        FileDescriptor descriptor = pfd.getFileDescriptor();

        // 根据 FileDescriptor 创建 InputStream
        InputStream input = new FileInputStream(descriptor);

        // 从 共享内存 中读取字节，并转为文字
        byte[] bytes = new byte[data.readInt()];

        try {
            // 读取共享内存中数据的大小
            int size = input.read(bytes);
            String message = new String(bytes, 0, size, StandardCharsets.UTF_8);
            WinkKVLog.i(TAG, "读取到另外一个进程写入的字符串：" + message);

            // 回复调用进程
            if (reply != null) {
                reply.writeString("Server 端收到 FileDescriptor, 并且从共享内存中读到了：" + message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

}
