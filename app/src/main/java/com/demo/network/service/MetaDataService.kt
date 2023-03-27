package com.demo.network.service

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
    fun uploadFileMetaData(@Body metaData: MediaFileMetaDataRequest): Call<MetaDataResponse>

    @POST("/photo")
    fun uploadFaceMetaData(@Body metaData: FaceScanMetaDataRequest): Call<MetaDataResponse>

}