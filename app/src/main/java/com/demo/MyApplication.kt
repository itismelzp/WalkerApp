package com.demo

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import androidx.work.Configuration
import androidx.work.WorkManager
import com.demo.base.ActivityLifecycle
import com.demo.base.helper.AppHelper
import com.demo.base.log.MyLog
import com.demo.base.log.MyLog.e
import com.demo.base.log.MyLog.init
import com.demo.customview.utils.ViewUtils
import com.demo.ipc.ProcessUtil
import com.demo.logger.LogUtil.DefaultLog
import com.demo.logger.LogUtil.TimberLog
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.cache.CacheFactory
import com.shuyu.gsyvideoplayer.player.PlayerFactory
import com.shuyu.gsyvideoplayer.utils.Debuger
import com.shuyu.gsyvideoplayer.utils.GSYVideoType
import com.tencent.mmkv.MMKV
//import com.tencent.shadow.core.common.LoggerFactory
//import com.tencent.shadow.dynamic.host.DynamicPluginManager
//import com.tencent.shadow.dynamic.host.DynamicRuntime
//import com.tencent.shadow.dynamic.host.PluginManager
//import com.tencent.shadow.sample.introduce_shadow_lib.AndroidLoggerFactory
//import com.tencent.shadow.sample.introduce_shadow_lib.FixedPathPmUpdater
import com.tencent.wink.storage.winkdb.log.WinkDbLog
import com.tencent.wink.storage.winkkv.WinkKVUtil
import com.walker.analytics.sdk.SensorsDataAPI
import tv.danmaku.ijk.media.exo2.Exo2PlayerManager
import tv.danmaku.ijk.media.exo2.ExoPlayerCacheManager
import java.io.File

//import com.demo.storage.EncoderUtil;
/**
 * Created by walkerzpli on 2020/9/23.
 */
class MyApplication : Application() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        Log.i(TAG, "[onCreate]")
    }

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "[onCreate]")
        instance = applicationContext
        ViewUtils.initContext(instance)
        AppHelper.init(this, BuildConfig.DEBUG)
        SensorsDataAPI.init(this)
        MMKV.initialize(this)
        //        EncoderUtil.init();
        WinkKVUtil.init(this, true)
        WinkDbLog.init(DefaultLog())
        init(TimberLog())
        // initPlugin(this);
        initVideoSdk()
        initWorkManager()
        e(TAG, "onCreate")
        ActivityLifecycle.addListener(object : ActivityLifecycle.LifecycleListener {
            override fun onForeground(activity: Activity) {
                MyLog.i(TAG, "onForeground")
            }

            override fun onBackground(activity: Activity) {
                MyLog.i(TAG, "onBackground")
            }

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                super.onActivityCreated(activity, savedInstanceState)
                MyLog.i(TAG, "onBackground")
            }

        })
        registerActivityLifecycleCallbacks(ActivityLifecycle)
    }

//    private fun initPlugin(application: Application) {
//        //Log接口Manager也需要使用，所以主进程也初始化。
//        LoggerFactory.setILoggerFactory(AndroidLoggerFactory())
//        if (ProcessUtil.isProcessByName(application, ":plugin")) {
//            //在全动态架构中，Activity组件没有打包在宿主而是位于被动态加载的runtime，
//            //为了防止插件crash后，系统自动恢复crash前的Activity组件，此时由于没有加载runtime而发生classNotFound异常，导致二次crash
//            //因此这里恢复加载上一次的runtime
//            DynamicRuntime.recoveryRuntime(application)
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//                WebView.setDataDirectorySuffix("plugin")
//            }
//        }
//        val fixedPathPmUpdater =
//            FixedPathPmUpdater(File("/data/local/tmp/plugin-manager-debug.apk"))
//        val needWaitingUpdate = (fixedPathPmUpdater.wasUpdating() //之前正在更新中，暗示更新出错了，应该放弃之前的缓存
//                || fixedPathPmUpdater.latest == null) //没有本地缓存
//        val update = fixedPathPmUpdater.update()
//        if (needWaitingUpdate) {
//            try {
//                update.get() //这里是阻塞的，需要业务自行保证更新Manager足够快。
//            } catch (e: Exception) {
//                throw RuntimeException("Sample程序不容错", e)
//            }
//        }
//        pluginManager = DynamicPluginManager(fixedPathPmUpdater)
//    }

    private fun initVideoSdk() {
        GSYVideoManager.instance().enableRawPlay(this)
        PlayerFactory.setPlayManager(Exo2PlayerManager::class.java)
        CacheFactory.setCacheManager(ExoPlayerCacheManager::class.java)
        GSYVideoType.enableMediaCodec()
        Debuger.enable()
    }

    private fun initWorkManager() {
        val config = Configuration.Builder()
            .setMinimumLoggingLevel(Log.INFO)
            .setJobSchedulerJobIdRange(1000, 2000)
            .build()
        WorkManager.initialize(this, config)
    }

    companion object {
        private const val TAG = "MyApplication"

        @SuppressLint("StaticFieldLeak")
        @JvmStatic
        lateinit var instance: Context
//        @JvmStatic
//        var pluginManager: PluginManager? = null
    }
}