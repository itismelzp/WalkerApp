package com.demo.network.model

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

/**
 * Created by lizhiping on 2023/3/7.
 * <p>
 * description
 */
data class MetaDataResponse(
    @SerializedName("cnt")
    var cnt: Int,
    var result: JsonObject
)
