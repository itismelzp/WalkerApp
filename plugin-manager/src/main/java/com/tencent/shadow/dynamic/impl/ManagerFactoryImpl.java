package com.tencent.shadow.dynamic.impl;

import android.content.Context;

import com.example.demo_manager.MyPluginManager;
import com.tencent.shadow.dynamic.host.ManagerFactory;
import com.tencent.shadow.dynamic.host.PluginManagerImpl;

/**
 * Created by lizhiping on 2022/11/25.
 * <p>
 * 此类包名及类名固定
 */
public class ManagerFactoryImpl implements ManagerFactory {
    @Override
    public PluginManagerImpl buildManager(Context context) {
        return new MyPluginManager(context);
    }
}
