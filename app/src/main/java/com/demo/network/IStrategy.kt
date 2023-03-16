package com.demo.network

import com.demo.network.model.SearchMediaItem
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

class NeedInLocalUniversalStrategy: IStrategy<SearchMediaItem> {
    override fun merge(
        localData: List<SearchMediaItem>,
        cloudData: List<SearchMediaItem>,
        universalMediaIds: List<Int>?
    ): List<SearchMediaItem> {
        return universalMediaIds?.run {
            DataConverter.dataFoldBySearchResultKey(
                localData + DataConverter.mediaItemListFilter(cloudData, this)
            )
        } ?: localData
    }

    @CloudDataMergeType
    override fun getSupportedType(): Int {
        return CloudDataMergeType.NEED_IN_LOCAL_UNIVERSAL
    }

}

class IntersectionStrategy: IStrategy<SearchMediaItem> {
    override fun merge(
        localData: List<SearchMediaItem>,
        cloudData: List<SearchMediaItem>,
        universalMediaIds: List<Int>?
    ): List<SearchMediaItem> {
        return DataConverter.dataFoldBySearchResultKey(localData + cloudData)
    }

    @CloudDataMergeType
    override fun getSupportedType(): Int {
        return CloudDataMergeType.INTERSECTION
    }

}

class LocalOnlyStrategy : IStrategy<SearchMediaItem> {
    override fun merge(
        localData: List<SearchMediaItem>,
        cloudData: List<SearchMediaItem>,
        universalMediaIds: List<Int>?
    ): List<SearchMediaItem> {
        return localData
    }

    @CloudDataMergeType
    override fun getSupportedType(): Int {
        return CloudDataMergeType.LOCAL_ONLY
    }

}

class CloudOnlyStrategy : IStrategy<SearchMediaItem> {
    override fun merge(
        localData: List<SearchMediaItem>,
        cloudData: List<SearchMediaItem>,
        universalMediaIds: List<Int>?
    ): List<SearchMediaItem> {
        return cloudData
    }

    @CloudDataMergeType
    override fun getSupportedType(): Int {
        return CloudDataMergeType.CLOUD_ONLY
    }

}