package com.demo.network.model

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken

/**
 * Created by lizhiping on 2023/3/7.
 * <p>
 * description
 */
data class MetaDataResponse(
    @SerializedName("uid")
    var userId: String,
    @SerializedName("cnt")
    var cnt: Int,
    @SerializedName("result")
    var result: JsonObject? // {111:1, 222:0, 333:1, 444:0}
) {
    fun parseResult(): Map<String, Int>? = Gson().fromJson(
        result?.toString() ?: "",
        object : TypeToken<Map<String, Int>>() {}.type
    ) as? Map<String, Int>

}
