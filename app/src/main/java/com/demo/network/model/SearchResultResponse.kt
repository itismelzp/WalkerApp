package com.demo.network.model

import com.google.gson.annotations.SerializedName

/**
 * Created by lizhiping on 2023/3/7.
 * <p>
 * description
 */
data class SearchResultResponse(
    @SerializedName("num_hits")
    var numHits: Int,
    var hits: List<Int>,
    @SerializedName("elapsed_time_micros")
    var elapsedTimeMicros: Int,
    var errors: List<String>,
    var aggregations: Data,
) {

    data class Data(
        var meta: List<String>,
        var data: List<MediaPath>
    )
}
