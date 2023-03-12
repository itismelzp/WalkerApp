package com.demo.network.type

import androidx.annotation.IntDef

/**
 * Created by lizhiping on 2023/3/2.
 * <p>
 * description
 */

/*
   |<-------------------- Local Universal: {1,2,3} ------------------->|
   +--------------------------------+----------------------------------+
   |                                |  Local = {1,2}, Cloud = {2,3,4}  |
   +--------------------------------+----------------------------------+
   | type = NEED_IN_LOCAL_UNIVERSAL |  Merge Result = {1,2,3}          |
   +--------------------------------+----------------------------------+
   | type = INTERSECTION            |  Merge Result = {1,2,3,4}        |
   +--------------------------------+----------------------------------+
*/

@Retention(AnnotationRetention.SOURCE)
@IntDef(
    CloudDataMergeType.NEED_IN_LOCAL_UNIVERSAL,
    CloudDataMergeType.INTERSECTION,
    CloudDataMergeType.LOCAL_ONLY,
    CloudDataMergeType.CLOUD_ONLY
)
annotation class CloudDataMergeType {
    companion object {
        const val NEED_IN_LOCAL_UNIVERSAL   = 0 // 过滤不在端侧全集的数据
        const val INTERSECTION              = 1 // 合并端侧数据
        const val LOCAL_ONLY                = 2 // 只取端侧数据
        const val CLOUD_ONLY                = 3 // 只取云侧数据
    }
}
