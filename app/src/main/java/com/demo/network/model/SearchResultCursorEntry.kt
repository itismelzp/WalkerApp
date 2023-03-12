package com.demo.network.model;

/**
 * Created by lizhiping on 2023/3/12.
 * <p>
 * description
 */
data class SearchResultCursorEntry(
    var id: Int,
    var coverId: Int,
    var name: String,
    var relation: Int,
    var count: Int,
    var idList: String,
    var type: Int,
    var albumType: Int,
    var mediaType: Int,
)
