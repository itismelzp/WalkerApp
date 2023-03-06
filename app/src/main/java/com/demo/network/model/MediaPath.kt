package com.demo.network.model

/**
 * Created by lizhiping on 2023/3/7.
 * <p>
 * description
 */
data class MediaPath(
    var type: Int,
    var groupId: String,
    var albumType: Int,
    var mediaId: Int,
    var mediaType: Int,
    var dataTaken: Long
)
