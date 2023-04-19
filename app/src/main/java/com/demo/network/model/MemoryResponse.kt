package com.demo.network.model

import com.google.gson.annotations.SerializedName

/**
 * Created by lizhiping on 2023/4/12.
 * <p>
 * description
 */
data class MemoryResponse(
    @SerializedName("_id")
    var id: String,
    @SerializedName("imageFileids")
    var imageFileIds: List<String>,
    @SerializedName("keyword")
    var keyword: String,
    @SerializedName("mode")
    var mode: String,
    @SerializedName("theme")
    var theme: String,
    @SerializedName("timeEnd")
    var timeEnd: Int,
    @SerializedName("timeStart")
    var timeStart: Int,
    @SerializedName("userid")
    var userid: String,
    @SerializedName("videoPath")
    var videoPath: String
)