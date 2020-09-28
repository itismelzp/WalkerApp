package com.demo.customview;

import android.app.Application;
import android.content.Context;

import com.demo.customview.utils.ViewUtils;

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
    }

    public static Context getInstance() {
        return mContext;
    }

}
