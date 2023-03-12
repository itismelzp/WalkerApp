package com.demo.network

import com.demo.network.model.FaceScanMetaDataRequest
import com.demo.network.model.MediaFileMetaDataRequest
import com.demo.network.model.MetaDataResponse
import com.demo.network.model.SearchRequest
import com.demo.network.model.SearchResultResponse
import com.demo.network.service.MetaDataService
import com.demo.network.service.SearchService
import com.demo.network.utils.GsonUtil
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
class RequestAccessManager {

    companion object {
        private const val TAG = "MetaDataManager"
        private const val META_BASE_URL = "http://dy-qa-cn.heytapmobi.com"
        private const val SEARCH_BASE_URL = "http://dy-qa-cn.heytapmobi.com"

        private const val CONNECT_TIME_OUT = 5000L
        private const val READ_TIME_OUT = 5000L
        private const val WRITE_TIME_OUT = 90000L

        val INSTANCE by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            RequestAccessManager()
        }
    }

    private val metaDataService: MetaDataService
    private val searchService: SearchService
    private val retrofit: Retrofit
    private val searchRetrofit: Retrofit

    init {
        val okHttpClient = initHttpClient()
        retrofit = initRetrofit(okHttpClient, META_BASE_URL)
        searchRetrofit = initRetrofit(okHttpClient, SEARCH_BASE_URL)
        metaDataService = retrofit.create(MetaDataService::class.java)
        searchService = searchRetrofit.create(SearchService::class.java)
    }

    private fun initHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIME_OUT, TimeUnit.MILLISECONDS)
            .readTimeout(READ_TIME_OUT, TimeUnit.MILLISECONDS)
            .writeTimeout(WRITE_TIME_OUT, TimeUnit.MILLISECONDS)
            .build()
    }

    private fun initRetrofit(okHttpClient: OkHttpClient, url: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(url)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addCallAdapterFactory(Java8CallAdapterFactory.create())
            .addCallAdapterFactory(GuavaCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(GsonUtil.getGson()))
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

    fun search(request: SearchRequest, callBack: Callback<SearchResultResponse>) {
        searchService.search(
            request.query,
            request.maxHits,
            request.src
        ).enqueue(callBack)
    }

    suspend fun coroutineSearch(request: SearchRequest): SearchResultResponse {
        return searchService.coroutineSearch(
            request.query,
            request.maxHits,
            request.src
        )
    }

}