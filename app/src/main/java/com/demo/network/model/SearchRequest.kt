package com.demo.network.model

/**
 * Created by lizhiping on 2023/3/7.
 * <p>
 * description
 */
data class SearchRequest(
    var query: String,
    var maxHits: Int,
    var src: String
)
