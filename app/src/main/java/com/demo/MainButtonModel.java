package com.demo;

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
import com.demo.ipc.IPCDemoActivity;
import com.demo.rxjava.RxJavaActivity;
import com.demo.storage.RoomActivity;
import com.demo.storage.WinkKVDemoActivity;
import com.demo.widget.activity.ScaleActivity;
import com.demo.widget.activity.ShapeBgActivity;
import com.demo.wink.WinkActivity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MainButtonModel {

    public static void initData(MainButtonViewModel mainButtonViewModel) {

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
        buttons.add(new MainButton("动画demo",  MainButtonType.TYPE_SYSTEM_VIEW, AnimatorActivity.class));
        buttons.add(new MainButton("rxjava demo", RxJavaActivity.class));
        buttons.add(new MainButton("ipc demo", IPCDemoActivity.class));
        buttons.sort(Comparator.comparingInt(o -> o.type));
        mainButtonViewModel.getMainButtonList().postValue(buttons);
    }

}
