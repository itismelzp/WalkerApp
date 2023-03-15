package com.demo.network.model

import com.google.gson.annotations.SerializedName

/**
 * Created by lizhiping on 2023/3/7.
 * <p>
 * description
 */
data class MediaPath(
    var mediaId: Int,
    var dataToken: Long,
    @SerializedName("type_")
    var types: List<Int>,
    @SerializedName("name")
    var names: List<List<String>>,
    @SerializedName("subName")
    var subNames: List<List<String>>
)
