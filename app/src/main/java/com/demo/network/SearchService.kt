package com.demo.network

import com.demo.network.model.SearchResultResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by lizhiping on 2023/3/7.
 * <p>
 * description
 */
interface SearchService {

    @GET("/photosearch/api/v1/album-000001/search")
    fun search(
        @Query("query") query: String,
        @Query("max_hits") maxHits: Int,
        @Query("src") src: String
    ): Call<SearchResultResponse>

}