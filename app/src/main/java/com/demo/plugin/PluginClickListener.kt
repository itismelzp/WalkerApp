//package com.demo.plugin
//
//import android.os.Bundle
//import android.view.View
//import com.demo.MainButton
//import com.demo.MyApplication
//import com.demo.constant.Constant.FROM_ID_START_ACTIVITY
//import com.demo.base.log.MyLog
//import com.tencent.shadow.dynamic.host.EnterCallback
//
///**
// * Created by lizhiping on 2023/2/7.
// * <p>
// * description
// */
//class PluginClickListener : MainButton.OnClickListener {
//
//    companion object {
//        private const val TAG = "PluginClickListener"
//    }
//
//    override fun onClickListener() {
//
//        val pluginManager = MyApplication.pluginManager
//        val bundle = Bundle()
//        // 插件 zip，这几个参数也都可以不传，直接在 PluginManager 中硬编码
//        bundle.putString("plugin_path", "/data/local/tmp/plugin-debug.zip")
//        // partKey 每个插件都有自己的 partKey 用来区分多个插件，如何配置在下面讲到
//        bundle.putString("part_key", "my-plugin")
//        // 路径举例：com.google.samples.apps.sunflower.GardenActivity
//        bundle.putString("activity_class_name", "com.example.demo_plugin.MainActivity")
//        // 要传入到插件里的参数
//        bundle.putBundle("extra_to_plugin_bundle", Bundle())
//
//        pluginManager?.enter(
//            MyApplication.instance,
//            FROM_ID_START_ACTIVITY,
//            bundle,
//            object : EnterCallback {
//                override fun onShowLoadingView(view: View) {
//                    MyLog.i(TAG, "[onShowLoadingView]")
//                }
//
//                override fun onCloseLoadingView() {
//                    MyLog.i(TAG, "[onCloseLoadingView]")
//                }
//
//                override fun onEnterComplete() {
//                    // 启动成功
//                    MyLog.i(TAG, "[onEnterComplete]")
//                }
//            })
//    }
//}