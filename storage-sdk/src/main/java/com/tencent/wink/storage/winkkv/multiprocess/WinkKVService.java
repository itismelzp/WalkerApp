package com.tencent.wink.storage.winkkv.multiprocess;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.tencent.wink.storage.winkkv.WinkKV;
import com.tencent.wink.storage.winkkv.log.WinkKVLog;


public class WinkKVService extends Service {

    private static final String TAG = "WinkKVService";

    private IBinder mBinder;
    private WinkKV mWinkKV;

    public WinkKVService() {
        mBinder = new WinkKVBinder(mCallbackOnBind);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private final CallbackOnBind mCallbackOnBind = new CallbackOnBind() {

        @Override
        public void onPut(String key, Object value) {
            WinkKVLog.i(TAG, "key：" + key + "value: " + value);
        }

        @Override
        public void onRemove(String key) {
            WinkKVLog.i(TAG, "key：" + key);
        }

    };

    public interface CallbackOnBind {
        void onPut(String key, Object value);

        void onRemove(String key);
    }
}