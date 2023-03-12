package com.demo.network.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder

/**
 * Created by lizhiping on 2023/3/9.
 * <p>
 * description
 */
object GsonUtil {

    fun getGson(): Gson {
        return GsonBuilder().serializeSpecialFloatingPointValues().create()
    }
}