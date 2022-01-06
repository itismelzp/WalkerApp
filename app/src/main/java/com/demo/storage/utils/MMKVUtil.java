package com.demo.storage.utils;

import com.tencent.mmkv.MMKV;

/**
 * Created by walkerzpli on 2022/1/6.
 */
public class MMKVUtil {

    public static MMKV getMultiProcessMMKV() {
        return MMKV.mmkvWithID("InterProcessKV", MMKV.MULTI_PROCESS_MODE);
    }

}
