package com.demo;

import android.app.Application;
import android.content.Context;

import com.demo.customview.utils.ViewUtils;
import com.demo.storage.EncoderUtil;
import com.tencent.mmkv.MMKV;
import com.walker.analytics.sdk.SensorsDataAPI;

/**
 * Created by walkerzpli on 2020/9/23.
 */
public class MyApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();
        ViewUtils.initContext(mContext);
//        SensorsDataAPI.init(this);
        MMKV.initialize(this);
        EncoderUtil.init();
    }

    public static Context getInstance() {
        return mContext;
    }

}
