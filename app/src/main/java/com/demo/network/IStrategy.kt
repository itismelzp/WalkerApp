package com.demo.network

import com.demo.network.model.MediaItem
import com.demo.network.type.CloudDataMergeType
import com.demo.network.utils.DataConverter

/**
 * Created by lizhiping on 2023/3/12.
 * <p>
 * description
 */
interface IStrategy<T> {

    fun merge(localData: List<T>, cloudData: List<T>, universalMediaIds: List<Int>?): List<T>

    @CloudDataMergeType
    fun getSupportedType(): Int

}

class NeedInLocalUniversalStrategy: IStrategy<MediaItem> {
    override fun merge(
        localData: List<MediaItem>,
        cloudData: List<MediaItem>,
        universalMediaIds: List<Int>?
    ): List<MediaItem> {
        return universalMediaIds?.run {
            DataConverter.dataFold(
                localData + DataConverter.mediaItemListFilter(cloudData, this)
            )
        } ?: localData
    }

    @CloudDataMergeType
    override fun getSupportedType(): Int {
        return CloudDataMergeType.NEED_IN_LOCAL_UNIVERSAL
    }

}

class IntersectionStrategy: IStrategy<MediaItem> {
    override fun merge(
        localData: List<MediaItem>,
        cloudData: List<MediaItem>,
        universalMediaIds: List<Int>?
    ): List<MediaItem> {
        return DataConverter.dataFold(localData + cloudData)
    }

    @CloudDataMergeType
    override fun getSupportedType(): Int {
        return CloudDataMergeType.INTERSECTION
    }

}

class LocalOnlyStrategy : IStrategy<MediaItem> {
    override fun merge(
        localData: List<MediaItem>,
        cloudData: List<MediaItem>,
        universalMediaIds: List<Int>?
    ): List<MediaItem> {
        return localData
    }

    @CloudDataMergeType
    override fun getSupportedType(): Int {
        return CloudDataMergeType.LOCAL_ONLY
    }

}

class CloudOnlyStrategy : IStrategy<MediaItem> {
    override fun merge(
        localData: List<MediaItem>,
        cloudData: List<MediaItem>,
        universalMediaIds: List<Int>?
    ): List<MediaItem> {
        return cloudData
    }

    @CloudDataMergeType
    override fun getSupportedType(): Int {
        return CloudDataMergeType.CLOUD_ONLY
    }

}