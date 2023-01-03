package com.demo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.demo.animator.AnimatorActivity;
import com.demo.apt.AptDemoActivity;
import com.demo.customview.activity.CustomMatrixActivity;
import com.demo.customview.activity.CustomShaderActivity;
import com.demo.customview.activity.ListViewDemoActivity;
import com.demo.customview.activity.SlideConflictDemoActivity;
import com.demo.customview.aige.activity.AigeActivity;
import com.demo.customview.ryg.ViewDispatchDemoActivity;
import com.demo.customview.sloop.activity.CustomSloopMenuActivity;
import com.demo.customview.zhy.activity.CustomViewActivity;
import com.demo.fragment.BlankFragment;
import com.demo.ipc.IPCDemoActivity;
import com.demo.logger.LoggerActivity;
import com.demo.rxjava.RxJavaActivity;
import com.demo.storage.RoomActivity;
import com.demo.storage.WinkKVDemoActivity;
import com.demo.widget.activity.ScaleActivity;
import com.demo.widget.activity.ShapeBgActivity;
import com.demo.wink.WinkActivity;
import com.tencent.shadow.dynamic.host.EnterCallback;
import com.tencent.shadow.dynamic.host.PluginManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MainButtonModel {

    private static final String TAG = "MainButtonModel";

    private static WeakReference<AppCompatActivity> mActivityRef;

    public static void initData(MainButtonViewModel mainButtonViewModel, AppCompatActivity activity) {
        mActivityRef = new WeakReference<>(activity);
        List<MainButton> buttons = new ArrayList<>();
        buttons.add(new MainButton("custom view", MainButtonType.TYPE_CUSTOM_VIEW, CustomViewActivity.class));
        buttons.add(new MainButton("custom shader", MainButtonType.TYPE_CUSTOM_VIEW, CustomShaderActivity.class));
        buttons.add(new MainButton("sloop custom view", MainButtonType.TYPE_CUSTOM_VIEW, CustomSloopMenuActivity.class));
        buttons.add(new MainButton("custom matrix view", MainButtonType.TYPE_CUSTOM_VIEW, CustomMatrixActivity.class));
        buttons.add(new MainButton("aige custom view", MainButtonType.TYPE_CUSTOM_VIEW, AigeActivity.class));
        buttons.add(new MainButton("btn list view demo", MainButtonType.TYPE_SYSTEM_VIEW, ListViewDemoActivity.class));
        buttons.add(new MainButton("test recycleview", MainButtonType.TYPE_SYSTEM_VIEW, ScaleActivity.class));
        buttons.add(new MainButton("shape background", MainButtonType.TYPE_CUSTOM_VIEW, ShapeBgActivity.class));
        buttons.add(new MainButton("wink page", MainButtonType.TYPE_CUSTOM_VIEW, WinkActivity.class));
        buttons.add(new MainButton("storage page", MainButtonType.TYPE_STORAGE, RoomActivity.class));
        buttons.add(new MainButton("view 事件分发", ViewDispatchDemoActivity.class));
        buttons.add(new MainButton("test apt demo", MainButtonType.TYPE_COMPILE, AptDemoActivity.class));
        buttons.add(new MainButton("进度条滑动冲突", SlideConflictDemoActivity.class));
        buttons.add(new MainButton("wink kv demo", MainButtonType.TYPE_STORAGE, WinkKVDemoActivity.class));
        buttons.add(new MainButton("动画demo", MainButtonType.TYPE_SYSTEM_VIEW, AnimatorActivity.class));
        buttons.add(new MainButton("rxjava demo", RxJavaActivity.class));
        buttons.add(new MainButton("ipc demo", IPCDemoActivity.class));
        buttons.add(new MainButton("logger demo", LoggerActivity.class));
        buttons.add(new MainButton("plugin demo", pluginClickListener));
        buttons.add(new MainButton("fragment demo", fragmentClickListener));

        buttons.sort(Comparator.comparingInt(o -> o.type));
        mainButtonViewModel.getMainButtonList().postValue(buttons);
    }

    private static final MainButton.OnclickListener pluginClickListener = () -> {
        PluginManager pluginManager = MyApplication.getPluginManager();
        /**
         * @param context context
         * @param formId  标识本次请求的来源位置，用于区分入口
         * @param bundle  参数列表, 建议在参数列表加入自己的验证
         * @param callback 用于从PluginManager实现中返回View
         */
        Bundle bundle = new Bundle();
        // 插件 zip，这几个参数也都可以不传，直接在 PluginManager 中硬编码
        bundle.putString("plugin_path", "/data/local/tmp/plugin-debug.zip");
        // partKey 每个插件都有自己的 partKey 用来区分多个插件，如何配置在下面讲到
        bundle.putString("part_key", "my-plugin");
        // 路径举例：com.google.samples.apps.sunflower.GardenActivity
        bundle.putString("activity_class_name", "com.example.demo_plugin.MainActivity");
        // 要传入到插件里的参数
        bundle.putBundle("extra_to_plugin_bundle", new Bundle());

        pluginManager.enter(MyApplication.getInstance(), 1011L, bundle, new EnterCallback() {
            @Override
            public void onShowLoadingView(View view) {
                Log.i(TAG, "[onShowLoadingView]");
            }

            @Override
            public void onCloseLoadingView() {
                Log.i(TAG, "[onCloseLoadingView]");
            }

            @Override
            public void onEnterComplete() {
                // 启动成功
                Log.i(TAG, "[onEnterComplete]");
            }
        });
    };

    private static final MainButton.OnclickListener fragmentClickListener = () -> {

        if (getActivity() == null) {
            return;
        }
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container,
                        BlankFragment.newInstance("hello world", "hello fragment"),
                        "blockFragment")
                .addToBackStack("blockFragment")
                .commit();
    };

    private static AppCompatActivity getActivity() {
        if (mActivityRef == null || mActivityRef.get() == null) {
            return null;
        }
        return mActivityRef.get();
    }

}
