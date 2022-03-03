package com.tencent.wink.storage.winkkv;

import android.content.Context;
import android.content.Intent;

import com.tencent.wink.storage.winkkv.multiprocess.WinkKVService;
import com.tencent.wink.storage.winkkv.multiprocess.WinkKVServiceConnection;
import com.tencent.wink.storage.winkkv.multiprocess.WinkMPKV;


/**
 * Created by walkerzpli on 2022/1/6.
 */
public class WinkKVUtil {

    private static final String NAME = "common_store";

    public static void init(Context context, boolean enableMultiProcess) {
        if (enableMultiProcess) {
            Intent intent = new Intent(context, WinkKVService.class);
            context.bindService(intent, WinkKVServiceConnection.getInstance(), Context.BIND_AUTO_CREATE);
        }
    }

    public static WinkKV getWinkKV(Context context) {
        String path = context.getFilesDir().getAbsolutePath() + "/wink_kv";
        return new WinkKV.Builder(path, NAME).build();
    }

    public static WinkMPKV getWinkMPKV(Context context) {
        return new WinkMPKV(context);
    }

}
