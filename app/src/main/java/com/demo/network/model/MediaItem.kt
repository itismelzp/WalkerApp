package com.demo.network.model


/**
 * Created by lizhiping on 2023/3/12.
 * <p>
 * description
 */
data class MediaItem(
    var dataTaken: Long,
    var mediaIds: MutableList<Int>,
    var mediaPath: String,
    var name: String,
    var type: Int,

    // for SearchResultCursorEntry
    var id: Int = 0,
    var coverId: Int = 0,
    var relation: Int = 0,
    var count: Int = 0,
    var albumType: Int = 0,
    var mediaType: Int = 0
)