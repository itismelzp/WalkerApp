package com.demo.storage.utils;

import com.tencent.mmkv.MMKV;

/**
 * Created by walkerzpli on 2022/1/6.
 */
public class MMKVUtil {

    public static MMKV getMPMMKV() {
        return MMKV.mmkvWithID("InterProcessKV", MMKV.MULTI_PROCESS_MODE);
//        return MMKV.mmkvWithID("imported", MMKV.MULTI_PROCESS_MODE);
    }

    /**
     * single process
     * @return
     */
    public static MMKV getMMKV() {
        return MMKV.defaultMMKV();
    }

}
