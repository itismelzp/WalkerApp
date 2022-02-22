package com.tencent.wink.storage.winkkv.multiprocess;

import android.os.MemoryFile;
import android.os.ParcelFileDescriptor;

import com.tencent.wink.storage.winkkv.log.WinkKVLog;

import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * 匿名共享内存工具类
 * <p>
 * Created by walkerzpli on 2022/1/6.
 */
public class WinkAshmemUtil {

    private static final String TAG = "WinkAshmemUtil";

    public static final int TRANSACT_CODE = 920511;

    /**
     * 创建MemoryFile
     * @param bytes 传输的内容
     * @return
     */
    public static ParcelFileDescriptor createMemoryFile(byte[] bytes) {
        try {
            // 创建 MemoryFile 对象，1024 是最大占用内存的大小。
            MemoryFile file = new MemoryFile("WinkAshmemFile", 1024);

            // 获取文件描述符，因为方法被标注为 @hide，只能反射获取
            FileDescriptor descriptor = (FileDescriptor) invokeMethod("getFileDescriptor", file);

            // 如果获取失败，返回
            if (descriptor == null) {
                WinkKVLog.e(TAG, "获取匿名共享内存的 FileDescriptor 失败");
                return null;
            }

            // 往共享内存中写入数据
            file.writeBytes(bytes, 0, 0, bytes.length);

            // 因为要跨进程传递，需要序列化 FileDescriptor
            return ParcelFileDescriptor.dup(descriptor);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过反射执行 obj.name() 方法
     */
    private static Object invokeMethod(String name, MemoryFile obj) {
        try {
            Method method = obj.getClass().getDeclaredMethod(name);
            return method.invoke(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取进程ID
     */
    public static int getPid() {
        return android.os.Process.myPid();
    }

}
