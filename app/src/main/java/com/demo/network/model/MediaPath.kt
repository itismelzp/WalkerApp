package com.demo.network.model

import com.google.gson.annotations.SerializedName

/**
 * Created by lizhiping on 2023/3/7.
 * <p>
 * description
 */
data class MediaPath(
    var dataTaken: Long,
    var mediaId: Int,
    var mediaPath: String,
    var name: List<String>,
    @SerializedName("type_")
    var type: List<Int>,
)
