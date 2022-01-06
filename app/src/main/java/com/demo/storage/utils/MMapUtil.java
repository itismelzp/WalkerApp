package com.demo.storage.utils;

import android.os.MemoryFile;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * Created by walkerzpli on 2022/1/6.
 */
public class MMapUtil {

    private static byte[] bytes = "落霞与孤鹜齐飞，秋水共长天一色。".getBytes();

    private ParcelFileDescriptor createMemoryFile() {
        // 创建 MemoryFile 对象，1024 是最大占用内存的大小。
        MemoryFile file = null;
        try {
            file = new MemoryFile("TestAshmemFile", 1024);

            // 获取文件描述符，因为方法被标注为 @hide，只能反射获取
            FileDescriptor descriptor = (FileDescriptor) invokeMethod("getFileDescriptor", file);

            // 如果获取失败，返回
            if (descriptor == null) {
                Log.i("MMapUtil", "获取匿名共享内存的 FileDescriptor 失败");
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
    private Object invokeMethod(String name, MemoryFile obj) {
        Method method = null;
        try {
            method = obj.getClass().getDeclaredMethod(name);
            return method.invoke(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
