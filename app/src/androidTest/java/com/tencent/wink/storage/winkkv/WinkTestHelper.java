package com.tencent.wink.storage.winkkv;

import android.util.Log;

import androidx.annotation.NonNull;

import com.demo.MyApplication;
import com.tencent.wink.storage.winkkv.log.WinkKVLog;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class WinkTestHelper {

    private static final String TAG = "WinkTestHelper";

    public static final String DIR = MyApplication.getInstance().getFilesDir() + "/test/";

    static AtomicInteger gcCount = new AtomicInteger();
    static AtomicInteger truncateCount = new AtomicInteger();

    public static WinkKVLog.ILog logger = new WinkKVLog.ILog() {

        @Override
        public void v(String tag, String msg) {
            Log.v(tag, "verbose: " + msg);
        }

        @Override
        public void d(String tag, String msg) {
            Log.d(tag, "debug: " + msg);
        }

        @Override
        public void i(@NonNull String tag, @NonNull String msg) {
            Log.i(tag, "info: " + msg);
        }

        @Override
        public void w(String tag, String msg) {
            Log.w(tag, "warning, msg: " + msg);
        }

        @Override
        public void w(String tag, String msg, Throwable e) {
            Log.w(tag, "warning: " + e.getMessage() + ", msg: " + msg);
        }

        @Override
        public void e(String tag, String msg) {
        }

        @Override
        public void e(String tag, String msg, Throwable e) {
            Log.e(tag, "error: " + e.getMessage() + ", msg: " + msg);
        }

    };

    public static Set<String> makeStringSet() {
        Set<String> set = new LinkedHashSet<>();
        set.add("one");
        set.add("two");
        set.add("three");
        set.add(null);
        set.add("");
        set.add("six");
        return set;
    }

    public static String makeString(int size) {
        char[] a = new char[size];
        for (int i = 0; i < size; i++) {
            a[i] = 'a';
        }
        return new String(a);
    }
}
