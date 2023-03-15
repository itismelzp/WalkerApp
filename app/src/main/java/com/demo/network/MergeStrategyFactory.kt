package com.demo.network

import com.demo.network.model.SearchMediaItem
import com.demo.network.type.CloudDataMergeType

/**
 * Created by lizhiping on 2023/3/12.
 * <p>
 * description
 */
object MergeStrategyFactory {

    private val STRATEGIES by lazy {
        listOf(
            NeedInLocalUniversalStrategy(),
            IntersectionStrategy(),
            LocalOnlyStrategy(),
            CloudOnlyStrategy()
        )
    }

    fun getStrategy(@CloudDataMergeType mergeType: Int): IStrategy<SearchMediaItem> {
        STRATEGIES.forEach {
            if (it.getSupportedType() == mergeType) {
                return it
            }
        }
        return NeedInLocalUniversalStrategy()
    }

}