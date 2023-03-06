package com.demo.network.model

/**
 * Created by lizhiping on 2023/3/7.
 * <p>
 * description
 */
data class SearchResult(
    val meta: Map<String, String>,
    var data: List<MediaPath>
)
