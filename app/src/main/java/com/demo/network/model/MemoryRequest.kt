package com.demo.network.model

import com.google.gson.annotations.SerializedName

/**
 * Created by lizhiping on 2023/4/12.
 * <p>
 * description
 */
data class MemoryRequest(
    @SerializedName("userId")
    var userId: String,
    @SerializedName("theme")
    var theme: String
)
