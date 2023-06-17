package com.demo;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.webkit.WebView;

import androidx.work.Configuration;
import androidx.work.WorkManager;

import com.demo.base.ActivityLifecycle;
import com.demo.customview.utils.ViewUtils;
import com.demo.ipc.ProcessUtil;
import com.demo.logger.LogUtil;
import com.demo.base.log.MyLog;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.cache.CacheFactory;
import com.shuyu.gsyvideoplayer.player.PlayerFactory;
import com.shuyu.gsyvideoplayer.utils.Debuger;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.tencent.shadow.sample.introduce_shadow_lib.AndroidLoggerFactory;

import com.tencent.shadow.sample.introduce_shadow_lib.FixedPathPmUpdater;
//import com.demo.storage.EncoderUtil;
import com.tencent.mmkv.MMKV;
import com.tencent.shadow.core.common.LoggerFactory;
import com.tencent.shadow.dynamic.host.DynamicPluginManager;
import com.tencent.shadow.dynamic.host.DynamicRuntime;
import com.tencent.shadow.dynamic.host.PluginManager;
import com.tencent.wink.storage.winkdb.log.WinkDbLog;
import com.tencent.wink.storage.winkkv.WinkKVUtil;
import com.walker.analytics.sdk.SensorsDataAPI;

import java.io.File;
import java.util.concurrent.Future;

import tv.danmaku.ijk.media.exo2.Exo2PlayerManager;
import tv.danmaku.ijk.media.exo2.ExoPlayerCacheManager;

/**
 * Created by walkerzpli on 2020/9/23.
 */
public class MyApplication extends Application {

    private static final String TAG = "MyApplication";

    @SuppressLint("StaticFieldLeak")
    private static Context mContext;

    private static PluginManager sPluginManager;

    public static PluginManager getPluginManager() {
        return sPluginManager;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        Log.i(TAG, "[onCreate]");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "[onCreate]");

        mContext = getApplicationContext();
        ViewUtils.initContext(mContext);
        SensorsDataAPI.init(this);
        MMKV.initialize(this);
//        EncoderUtil.init();
        WinkKVUtil.init(this, true);
        WinkDbLog.init(new LogUtil.DefaultLog());

        MyLog.init(new LogUtil.TimberLog());
        initPlugin(this);

        initVideoSdk();

        initWorkManager();

        MyLog.e(TAG, "onCreate");

        registerActivityLifecycleCallbacks(ActivityLifecycle.INSTANCE);
    }

    public static Context getInstance() {
        return mContext;
    }

    private void initPlugin(Application application) {
        //Log接口Manager也需要使用，所以主进程也初始化。
        LoggerFactory.setILoggerFactory(new AndroidLoggerFactory());

        if (ProcessUtil.isProcessByName(application, ":plugin")) {
            //在全动态架构中，Activity组件没有打包在宿主而是位于被动态加载的runtime，
            //为了防止插件crash后，系统自动恢复crash前的Activity组件，此时由于没有加载runtime而发生classNotFound异常，导致二次crash
            //因此这里恢复加载上一次的runtime
            DynamicRuntime.recoveryRuntime(application);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                WebView.setDataDirectorySuffix("plugin");
            }
        }

        FixedPathPmUpdater fixedPathPmUpdater
                = new FixedPathPmUpdater(new File("/data/local/tmp/plugin-manager-debug.apk"));
        boolean needWaitingUpdate
                = fixedPathPmUpdater.wasUpdating()//之前正在更新中，暗示更新出错了，应该放弃之前的缓存
                || fixedPathPmUpdater.getLatest() == null;//没有本地缓存
        Future<File> update = fixedPathPmUpdater.update();
        if (needWaitingUpdate) {
            try {
                update.get();//这里是阻塞的，需要业务自行保证更新Manager足够快。
            } catch (Exception e) {
                throw new RuntimeException("Sample程序不容错", e);
            }
        }
        sPluginManager = new DynamicPluginManager(fixedPathPmUpdater);
    }

    private void initVideoSdk() {
        GSYVideoManager.instance().enableRawPlay(this);
        PlayerFactory.setPlayManager(Exo2PlayerManager.class);
        CacheFactory.setCacheManager(ExoPlayerCacheManager.class);
        GSYVideoType.enableMediaCodec();
        Debuger.enable();
    }

    private void initWorkManager() {
        Configuration config = new Configuration.Builder()
                .setMinimumLoggingLevel(Log.INFO)
                .setJobSchedulerJobIdRange(1000, 2000)
                .build();
        WorkManager.initialize(this, config);
    }
}
