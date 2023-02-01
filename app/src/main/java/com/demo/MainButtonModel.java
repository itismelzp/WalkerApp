package com.demo;

import static com.demo.constant.Constant.FROM_ID_START_ACTIVITY;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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
import com.demo.fragment.BaseFragment;
import com.demo.fragment.GridFragment;
import com.demo.fragment.ViewPagerCollectionFragment;
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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class MainButtonModel {

    private static final String TAG = "MainButtonModel";

    private final static int SLIDE_RIGHT_ENTER_ANIMATION = R.anim.coui_open_slide_enter;
    private final static int SLIDE_LEFT_EXIT_ANIMATION = R.anim.coui_open_slide_exit;
    private final static int SLIDE_LEFT_ENTER_ANIMATION = R.anim.coui_close_slide_enter;
    private final static int SLIDE_RIGHT_EXIT_ANIMATION = R.anim.coui_close_slide_exit;
    private final static int[] anim = new int[]{
            SLIDE_RIGHT_ENTER_ANIMATION,
            SLIDE_LEFT_EXIT_ANIMATION,
            SLIDE_LEFT_ENTER_ANIMATION,
            SLIDE_RIGHT_EXIT_ANIMATION
    };

    private Fragment fragment;

    private final List<MainButton> buttons = new ArrayList<>();
    private static final Map<Integer, List<MainButton>> typeMap = new HashMap<>();

    private static final MainButton.OnclickListener pluginClickListener = () -> {
        PluginManager pluginManager = MyApplication.getPluginManager();
        Bundle bundle = new Bundle();
        // 插件 zip，这几个参数也都可以不传，直接在 PluginManager 中硬编码
        bundle.putString("plugin_path", "/data/local/tmp/plugin-debug.zip");
        // partKey 每个插件都有自己的 partKey 用来区分多个插件，如何配置在下面讲到
        bundle.putString("part_key", "my-plugin");
        // 路径举例：com.google.samples.apps.sunflower.GardenActivity
        bundle.putString("activity_class_name", "com.example.demo_plugin.MainActivity");
        // 要传入到插件里的参数
        bundle.putBundle("extra_to_plugin_bundle", new Bundle());

        pluginManager.enter(MyApplication.getInstance(), FROM_ID_START_ACTIVITY, bundle, new EnterCallback() {
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

    private final MainButton.OnclickListener fragmentClickListener = () -> {

        // 注意:强烈建议对涉及多种动画类型的效果使用transitions，因为使用嵌套AnimationSet实例存在已知的问题。
        fragment.getParentFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
//                .setCustomAnimations( // 官方不推荐使用Animations，使用嵌套动画时有已知问题
//                        R.anim.slide_in, // enter
//                        R.anim.fade_out, // exit
//                        R.anim.fade_in, // popEnter
//                        R.anim.slide_out // popExit
//                )
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE) // 推荐使用Transition
                .replace(R.id.fragment_container,
                        GridFragment.newInstance("hello world", "hello fragment"), GridFragment.class.getSimpleName())
                .addToBackStack(null)
                .commit();
    };

    private class FragmentOnclickListener implements MainButton.OnclickListener {

        private final BaseFragment targetFragment;

        public FragmentOnclickListener(BaseFragment targetFragment) {
            this.targetFragment = targetFragment;
        }

        @Override
        public void onClickListener() {
            // 注意:强烈建议对涉及多种动画类型的效果使用transitions，因为使用嵌套AnimationSet实例存在已知的问题。
            fragment.getParentFragmentManager()
                    .beginTransaction()
                    .setReorderingAllowed(true)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE) // 推荐使用Transition
                    .replace(R.id.fragment_container,
                            targetFragment.createFragment("hello world", "hello fragment"),
                            targetFragment.getClass().getSimpleName())
                    .addToBackStack(null)
                    .commit();
        }
    }


    public MainButtonModel(Fragment fragment) {
        this.fragment = fragment;
        initData();
    }

    private void initStaticData() {
        List<MainButton> customViews = new ArrayList<>();
        customViews.add(new MainButton("custom view", MainButtonType.TYPE_CUSTOM_VIEW, CustomViewActivity.class));
        customViews.add(new MainButton("custom shader", MainButtonType.TYPE_CUSTOM_VIEW, CustomShaderActivity.class));
        customViews.add(new MainButton("sloop custom view", MainButtonType.TYPE_CUSTOM_VIEW, CustomSloopMenuActivity.class));
        customViews.add(new MainButton("custom matrix view", MainButtonType.TYPE_CUSTOM_VIEW, CustomMatrixActivity.class));
        customViews.add(new MainButton("aige custom view", MainButtonType.TYPE_CUSTOM_VIEW, AigeActivity.class));
        customViews.add(new MainButton("shape background", MainButtonType.TYPE_CUSTOM_VIEW, ShapeBgActivity.class));
        customViews.add(new MainButton("wink page", MainButtonType.TYPE_CUSTOM_VIEW, WinkActivity.class));
        typeMap.put(MainButtonType.TYPE_CUSTOM_VIEW, customViews);

        List<MainButton> systemViews = new ArrayList<>();
        systemViews.add(new MainButton("btn list view demo", MainButtonType.TYPE_SYSTEM_VIEW, ListViewDemoActivity.class));
        systemViews.add(new MainButton("test recycleview", MainButtonType.TYPE_SYSTEM_VIEW, ScaleActivity.class));
        systemViews.add(new MainButton("动画demo", MainButtonType.TYPE_SYSTEM_VIEW, AnimatorActivity.class));
        systemViews.add(new MainButton("ViewPager2 demo", MainButtonType.TYPE_SYSTEM_VIEW, new FragmentOnclickListener(ViewPagerCollectionFragment.newInstance())));
        typeMap.put(MainButtonType.TYPE_SYSTEM_VIEW, systemViews);

        List<MainButton> systemComponents = new ArrayList<>();
        systemComponents.add(new MainButton("fragment demo", MainButtonType.TYPE_SYSTEM_COMPONENT, fragmentClickListener));
        systemComponents.add(new MainButton("fragment demo2", MainButtonType.TYPE_SYSTEM_COMPONENT, new FragmentOnclickListener(new GridFragment())));
        typeMap.put(MainButtonType.TYPE_SYSTEM_COMPONENT, systemComponents);

        List<MainButton> storage = new ArrayList<>();
        storage.add(new MainButton("storage page", MainButtonType.TYPE_STORAGE, RoomActivity.class));
        storage.add(new MainButton("wink kv demo", MainButtonType.TYPE_STORAGE, WinkKVDemoActivity.class));
        typeMap.put(MainButtonType.TYPE_STORAGE, storage);

        List<MainButton> compiler = new ArrayList<>();
        compiler.add(new MainButton("test apt demo", MainButtonType.TYPE_COMPILE, AptDemoActivity.class));
        typeMap.put(MainButtonType.TYPE_COMPILE, compiler);

        List<MainButton> other = new ArrayList<>();
        other.add(new MainButton("view 事件分发", ViewDispatchDemoActivity.class));
        other.add(new MainButton("进度条滑动冲突", SlideConflictDemoActivity.class));
        other.add(new MainButton("rxjava demo", RxJavaActivity.class));
        other.add(new MainButton("ipc demo", IPCDemoActivity.class));
        other.add(new MainButton("logger demo", LoggerActivity.class));
        other.add(new MainButton("plugin demo", pluginClickListener));
        typeMap.put(MainButtonType.TYPE_OTHER, other);
    }


    public void initData() {
        initStaticData();

        if (typeMap.containsKey(MainButtonType.TYPE_CUSTOM_VIEW)) {
            buttons.addAll(typeMap.get(MainButtonType.TYPE_CUSTOM_VIEW));
        }
        if (typeMap.containsKey(MainButtonType.TYPE_STORAGE)) {
            buttons.addAll(typeMap.get(MainButtonType.TYPE_STORAGE));
        }
        if (typeMap.containsKey(MainButtonType.TYPE_COMPILE)) {
            buttons.addAll(typeMap.get(MainButtonType.TYPE_COMPILE));
        }
        if (typeMap.containsKey(MainButtonType.TYPE_SYSTEM_VIEW)) {
            buttons.addAll(typeMap.get(MainButtonType.TYPE_SYSTEM_VIEW));
        }
        if (typeMap.containsKey(MainButtonType.TYPE_OTHER)) {
            buttons.addAll(typeMap.get(MainButtonType.TYPE_OTHER));
        }
        if (typeMap.containsKey(MainButtonType.TYPE_SYSTEM_COMPONENT)) {
            buttons.addAll(typeMap.get(MainButtonType.TYPE_SYSTEM_COMPONENT));
        }
        buttons.sort(Comparator.comparingInt(o -> o.type));
    }

    public void operateData(@MainButtonType int type, boolean isCheck) {
        if (isCheck) {
            addData(type);
        } else {
            removeData(type);
        }
    }

    public void addData(@MainButtonType int type) {
        List<MainButton> mainButtons = typeMap.get(type);
        if (mainButtons == null || mainButtons.size() == 0) {
            return;
        }
        if (!hasDataByType(type)) {
            buttons.addAll(mainButtons);
        }
        sort();
    }

    public void removeData(@MainButtonType int type) {
        List<MainButton> mainButtons = typeMap.get(type);
        if (mainButtons == null || mainButtons.size() == 0) {
            return;
        }
        buttons.removeAll(mainButtons);
        sort();
    }

    public List<MainButton> getButtons() {
        return buttons;
    }

    public List<MainButton> getDiffButtons() {
        List<MainButton> ret = new ArrayList<>();
        TreeSet<Integer> types = new TreeSet<>(typeMap.keySet());
        for (Integer type : types) {
            List<MainButton> mainButtons = typeMap.get(type);
            if (mainButtons == null || mainButtons.size() == 0) {
                continue;
            }
            ret.add(mainButtons.get(0));
        }
        return ret;
    }

    private boolean hasDataByType(@MainButtonType int type) {
        for (MainButton button : buttons) {
            if (button.type == type) {
                return true;
            }
        }
        return false;
    }

    private void sort() {
        buttons.sort(Comparator.comparingInt(o -> o.type));
    }

}
