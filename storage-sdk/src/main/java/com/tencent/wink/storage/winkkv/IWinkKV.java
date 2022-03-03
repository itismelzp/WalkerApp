package com.tencent.wink.storage.winkkv;

import java.util.Map;

/**
 * description
 * <p>
 * Created by walkerzpli on 2022/3/2.
 */
public interface IWinkKV {

    void putAll(Map<String, Object> dataMap);

    Map<String, Object> getAll();

    void putString(String key, String value);

    String getString(String key);

    void putBoolean(String key, boolean value);

    boolean getBoolean(String key);

    void putInt(String key, int value);

    int getInt(String key);

    void putFloat(String key, float value);

    float getFloat(String key);

    void putDouble(String key, double value);

    double getDouble(String key);
}
