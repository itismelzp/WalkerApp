package com.demo.network.service

import com.demo.network.model.MemoryResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by lizhiping on 2023/4/12.
 * <p>
 * description
 */
interface MemoryService {

    @GET("/v1/history")
    fun fetchMemory(
        @Query("userId") userId: String,
        @Query("theme") theme: String,
    ): Call<List<MemoryResponse>>

    @GET("/v1/history")
    suspend fun coroutineFetchMemory(
        @Query("userId") userId: String,
        @Query("theme") theme: String,
    ): List<MemoryResponse>

}