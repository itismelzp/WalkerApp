package com.demo.network.model

/**
 * Created by lizhiping on 2023/3/7.
 * <p>
 * description
 */
data class FaceScanMetaData(
    var mediaId: Int,           // 媒体Id
    var fileId: String,         // ocs 文件Id
    var groupId: Int,           // 分组Id
    var groupName: String,      // 分组名称
    var groupRelation: Int,     // 与我的关系
    var relationDesc: String,   // 与我关系描述
    var rectLeft: Int,          // 矩形左边坐标
    var rectTop: Int,           // 矩形顶部坐标
    var rectRight: Int,         // 矩形右边坐标
    var rectBottom: Int,        // 矩形底部坐标
    var mediaType: Int,         // 媒体类型
    var thumbWidth: Int,        // 缩略图宽
    var thumbHeight: Int        // 缩略图高
)
