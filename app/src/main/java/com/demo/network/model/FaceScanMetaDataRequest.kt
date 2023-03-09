package com.demo.network.model

/**
 * Created by lizhiping on 2023/3/7.
 * <p>
 * description
 */
data class FaceScanMetaDataRequest(
    var userId: String,
    var faceScanMetaDatas: List<FaceScanMetaData>
)