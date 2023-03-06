package com.demo.network

import com.demo.network.model.FaceScanMetaDataRequest
import com.demo.network.model.MediaFileMetaDataRequest
import com.demo.network.model.MetaDataResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created by lizhiping on 2023/3/6.
 * <p>
 * description
 */
interface MetaDataService {

    @POST("/photo")
    fun uploadMetaData(@Body metaData: MediaFileMetaDataRequest): Call<MetaDataResponse>

    @POST("/photo")
    fun uploadMetaData(@Body metaData: FaceScanMetaDataRequest): Call<MetaDataResponse>

}