package com.demo.network.model

import com.google.gson.annotations.SerializedName

/**
 * Created by lizhiping on 2023/3/7.
 * <p>
 * description
 */
data class MediaPath(
    var day: Int,
    var latitude: Double,
    var longitude: Double,
    var month: Int,
    var name: String,
    @SerializedName("thumb_url")
    var thumbUrl: String,
    var timestamp: Long,
    var type_: List<Int>,
    var url: String,
    var year: Int
)
