package com.demo.storage.utils;

import android.content.Context;

import com.walker.storage.winkkv.WinkKV;

/**
 * Created by walkerzpli on 2022/1/6.
 */
public class WinkKVUtil {

    private static String NAME = "common_store";


    public static WinkKV getWinkKV(Context context) {
        String path = context.getFilesDir().getAbsolutePath() + "/wink_kv";
        return new WinkKV.Builder(path, NAME).build();
    }

}
