package com.demo.network.model

/**
 * Created by lizhiping on 2023/3/6.
 * <p>
 * description
 */
data class MediaFileMetaDataRequest(
    var userId: String,
    var mediaFileMetaDatas: List<MediaFileMetaData>
)

