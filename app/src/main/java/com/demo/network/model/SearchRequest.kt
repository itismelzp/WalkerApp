package com.demo.network.model

import com.google.gson.annotations.SerializedName

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

data class SearchResultResponse(
    @SerializedName("num_hits")
    var numHits: Int,
    var hits: List<Any>,
    @SerializedName("elapsed_time_micros")
    var elapsedTimeMicros: Long,
    var errors: List<Any>,
    var aggregations: Aggregation
) {

    data class Aggregation(
        var data: List<MediaPath>,
        var meta: List<String>
    )
}
