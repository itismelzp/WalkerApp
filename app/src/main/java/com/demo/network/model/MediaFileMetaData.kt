package com.demo.network.model

/**
 * Created by lizhiping on 2023/3/6.
 * <p>
 * description
 */
data class MediaFileMetaData(
    var createTime: Long,
    var fileId: String,
    var fileMD5: String,
    var fileMediaType: Int,
    var filePath: String,
    var mediaId: Int,
    val latitude: Double,
    var longitude: Double,
    var mediaType: Int,
    var width: Int,
    var height: Int
)
