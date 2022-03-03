package com.tencent.wink.storage.winkkv;

import android.content.Context;
import android.content.Intent;

import com.tencent.wink.storage.winkkv.multiprocess.IWinkKV;
import com.tencent.wink.storage.winkkv.multiprocess.WinkKVService;
import com.tencent.wink.storage.winkkv.multiprocess.WinkKVServiceConnection;

import java.util.Map;

/**
 * description
 * <p>
 * Created by walkerzpli on 2022/3/2.
 */
public class WinkMPKV implements IWinkKV {

    private static final String TAG = "WinkMPKV";
    private final WinkKVServiceConnection mServiceConnection = WinkKVServiceConnection.getInstance();

    public WinkMPKV(Context context) {
        Intent intent = new Intent(context, WinkKVService.class);
//        intent.setAction("com.tencent.wink.storage.winkkv.multiprocess.WinkKVService");
//        intent.setPackage("com.demo"); // TODO::所在进程的包名
        context.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void putAll(Map<String, Object> dataMap) {
        mServiceConnection.putAll(dataMap);
    }

    @Override
    public Map<String, Object> getAll() {
        return mServiceConnection.getAll();
    }

    @Override
    public void putString(String key, String value) {
        mServiceConnection.putString(key, value);
    }

    @Override
    public String getString(String key) {
        return mServiceConnection.getString(key);
    }

    @Override
    public void putBoolean(String key, boolean value) {
        mServiceConnection.putBoolean(key, value);
    }

    @Override
    public boolean getBoolean(String key) {
        return mServiceConnection.getBoolean(key);
    }

    @Override
    public void putInt(String key, int value) {
        mServiceConnection.putInt(key, value);
    }

    @Override
    public int getInt(String key) {
        return mServiceConnection.getInt(key);
    }

    @Override
    public void putFloat(String key, float value) {
        mServiceConnection.putFloat(key, value);
    }

    @Override
    public float getFloat(String key) {
        return mServiceConnection.getFloat(key);
    }

    @Override
    public void putDouble(String key, double value) {
        mServiceConnection.putDouble(key, value);
    }

    @Override
    public double getDouble(String key) {
        return mServiceConnection.getDouble(key);
    }
}
