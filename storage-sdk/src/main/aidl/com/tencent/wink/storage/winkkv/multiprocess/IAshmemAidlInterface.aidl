// IAshmemAidlInterface.aidl
package com.tencent.wink.storage.winkkv.multiprocess;

// Declare any non-default types here with import statements

interface IAshmemAidlInterface {

    void putAll(in Map map);

    Map getAll();

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