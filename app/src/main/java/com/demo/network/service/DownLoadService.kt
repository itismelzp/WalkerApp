package com.demo.network.service

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

/**
 * Created by lizhiping on 2023/4/12.
 * <p>
 * description
 */
interface DownLoadService {

    @GET("/andes-photo/memory-video/785515850_StoryChildren_20230404060647_test.mp4")
    fun downLoadFile(): Call<ResponseBody>

    @GET
    fun downloadFileSync(@Url fileUrl: String): Call<ResponseBody>

}