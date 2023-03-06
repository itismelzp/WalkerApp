package com.demo.network

import com.demo.network.model.*
import okhttp3.OkHttpClient
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.adapter.guava.GuavaCallAdapterFactory
import retrofit2.adapter.java8.Java8CallAdapterFactory
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by lizhiping on 2023/3/6.
 * <p>
 * description
 */
class MediaFileMetaDataManager {

    companion object {
        private const val TAG = "MetaDataManager"
        private const val BASE_URL = "http://dy-qa-cn.heytapmobi.com"

        private const val CONNECT_TIME_OUT = 30L
        private const val READ_TIME_OUT = 60L
        private const val WRITE_TIME_OUT = 90L

        val INSTANCE by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            MediaFileMetaDataManager()
        }
    }

    private val metaDataService: MetaDataService
    private val retrofit: Retrofit

    init {
        val okHttpClient = initHttpClient()
        retrofit = initRetrofit(okHttpClient)
        metaDataService = retrofit.create(MetaDataService::class.java)
    }

    private fun initHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
            .build()
    }

    private fun initRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addCallAdapterFactory(Java8CallAdapterFactory.create())
            .addCallAdapterFactory(GuavaCallAdapterFactory.create())
//            .addConverterFactory(NullOnEmptyConverterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }


    fun uploadMetaData(metaData: MediaFileMetaDataRequest, callback: Callback<MetaDataResponse>) {
        val call = metaDataService.uploadMetaData(metaData)
        call.enqueue(callback)
    }

    fun uploadMetaData(metaData: FaceScanMetaDataRequest, callback: Callback<MetaDataResponse>) {
        val call = metaDataService.uploadMetaData(metaData)
        call.enqueue(callback)
    }

}