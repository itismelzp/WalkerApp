package com.demo;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.demo.customview.utils.ViewUtils;
import com.demo.storage.EncoderUtil;
import com.tencent.mmkv.MMKV;
import com.tencent.wink.storage.winkdb.log.WinkDbLog;
import com.tencent.wink.storage.winkkv.WinkKVUtil;
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
        SensorsDataAPI.init(this);
        MMKV.initialize(this);
        EncoderUtil.init();
        WinkKVUtil.init(this, true);
        WinkDbLog.init(new WalkerLog());
    }

    public static Context getInstance() {
        return mContext;
    }

    private static class WalkerLog implements WinkDbLog.ILog {
        @Override
        public void v(String tag, String msg) {
            Log.v(tag, msg);
        }

        @Override
        public void d(String tag, String msg) {
            Log.d(tag, msg);
        }

        @Override
        public void i(String tag, String msg) {
            Log.i(tag, msg);
        }

        @Override
        public void w(String tag, String msg) {
            Log.w(tag, msg);
        }

        @Override
        public void w(String tag, String msg, Throwable t) {
            Log.w(tag, msg, t);
        }

        @Override
        public void e(String tag, String msg) {
            Log.e(tag, msg);
        }

        @Override
        public void e(String tag, String msg, Throwable t) {
            Log.e(tag, msg, t);
        }
    }

}
