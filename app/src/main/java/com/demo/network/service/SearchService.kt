package com.demo.network.service

import com.demo.network.model.SearchResultResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by lizhiping on 2023/3/7.
 * <p>
 * description
 */
interface SearchService {

    @HTTP(method = "GET", path = "/photosearch/api/v1/album-{userId}/search")
    suspend fun coroutineSearchWithUserId(
        @Path("userId") userId: String,
        @Query("query") query: String,
        @Query("max_hits") maxHits: Int,
        @Query("src") src: String
    ): SearchResultResponse

    @GET("/photosearch/api/v1/album-{userId}/search")
    fun searchWithUserId(
        @Path("userId") userId: String,
        @Query("query") query: String,
        @Query("max_hits") maxHits: Int,
        @Query("src") src: String
    ): Call<SearchResultResponse>

    @GET("/photosearch/api/v1/album-100k/search")
    fun search(
        @Query("query") query: String,
        @Query("max_hits") maxHits: Int,
        @Query("src") src: String
    ): Call<SearchResultResponse>

    @GET("/photosearch/api/v1/album-000001/search")
    suspend fun coroutineSearch(
        @Query("query") query: String,
        @Query("max_hits") maxHits: Int,
        @Query("src") src: String
    ): SearchResultResponse

}