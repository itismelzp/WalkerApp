package com.tencent.shadow.sample.introduce_shadow_lib;

import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.util.Log;

import com.tencent.shadow.dynamic.host.PluginProcessService;


/**
 * Created by lizhiping on 2022/12/3.
 * <p>
 * 一个PluginProcessService（简称PPS）代表一个插件进程。插件进程由PPS启动触发启动。
 * 新建PPS子类允许一个宿主中有多个互不影响的插件进程。
 */
public class MainPluginProcessService extends PluginProcessService {

    private static final String TAG = "MainPluginProcessServic";

    public MainPluginProcessService() {

        LoadPluginCallback.setCallback(new LoadPluginCallback.Callback() {
            @Override
            public void beforeLoadPlugin(String partKey) {
                Log.e(TAG, "beforeLoadPlugin====>" +
                        "\tpartKey = [" + partKey + "]");
            }

            @Override
            public void afterLoadPlugin(String partKey, ApplicationInfo applicationInfo, ClassLoader pluginClassLoader, Resources pluginResources) {
                Log.e(TAG, "afterLoadPlugin=====>" +
                        "\tpartKey = [" + partKey + "], " +
                        "applicationInfo.className = [" + applicationInfo.className + "], " +
                        "applicationInfo.metaData = [" + applicationInfo.metaData + "], " +
                        "pluginClassLoader = [" + pluginClassLoader + "]," +
                        "pluginResources = [" + pluginResources + "]");
            }
        });
    }
}