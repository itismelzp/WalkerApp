package com.demo.network.model


/**
 * Created by lizhiping on 2023/3/12.
 * <p>
 * description
 */
data class SearchMediaItem(
    var type: Int,
    var isChildType: Boolean,
    var mediaIds: MutableList<Int>,
    var name: String, // name or subName

    // for SearchResultCursorEntry
    var id: Int = 0,
    var coverId: Int = 0,
    var relation: Int = 0,
    var count: Int = 0,
    var albumType: Int = 0,
    var mediaType: Int = 0
)