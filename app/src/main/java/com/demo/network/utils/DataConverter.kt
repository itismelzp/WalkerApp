package com.demo.network.utils

import com.demo.network.model.DataCreator
import com.demo.network.model.SearchMediaItem
import com.demo.network.model.MediaPath
import com.demo.network.model.SearchResultKey
import com.demo.network.model.SearchResultResponse
import com.google.gson.Gson

/**
 * Created by lizhiping on 2023/3/12.
 * <p>
 * description
 */
object DataConverter {


    const val LABEL = 5
    const val CHILD_LABEL = 8

    @JvmStatic
    fun main(args: Array<String>) {
        val aggregations = Gson().fromJson(DataCreator.SEARCH_MEDIA_PATH_LIST, SearchResultResponse.Aggregation::class.java)
        val dataList: List<MediaPath> = aggregations.data
        println("dataList: $dataList")
        val mediaItemList = mediaPathList2MediaItemList(dataList)
        println("mediaItemList: $mediaItemList")
    }

    private fun LinkedHashMap<SearchResultKey, SearchMediaItem>.putAndComb(item: SearchMediaItem) {
        val key = SearchResultKey(item.type, item.name)
        if (!containsKey(key)) {
            put(key, item)
        } else {
            get(key)?.run {
                mediaIds.addAll(item.mediaIds)
                count = mediaIds.size
            }
        }
    }

    private fun LinkedHashMap<SearchResultKey, SearchMediaItem>.putAndCombAll(
        itemList: List<SearchMediaItem>
    ) {
        itemList.forEach {
            putAndComb(it)
        }
    }

    fun mediaPathList2MediaItemList(dataList: List<MediaPath>): List<SearchMediaItem> {
        val mainMediaItemMap = LinkedHashMap<SearchResultKey, SearchMediaItem>()

        // 1）先取出subName，并为每个suName创建SearchMediaItem
        val subMediaItemMap = LinkedHashMap<SearchResultKey, SearchMediaItem>()
        for (data in dataList) {
            if (data.subNames == null || data.subNames.isEmpty()) {
                continue
            }
            val flattenSubNames: List<String> = data.subNames.flatten()
            flattenSubNames.forEach {
                subMediaItemMap.putAndComb(
                    SearchMediaItem(
                        type = CHILD_LABEL,
                        isChildType = true,
                        mediaIds = mutableListOf(data.mediaId),
                        name = it,
                        coverId = data.mediaId,
                        count = 1,
                        mediaType = 1
                    )
                );
            }
        }

        for (data in dataList) {
            data.types.forEachIndexed { index, type ->
                val typeNames = data.names[index]
                for (name in typeNames) {
                    mainMediaItemMap.putAndComb(
                        SearchMediaItem(
                            type = type,
                            isChildType = false,
                            mediaIds = mutableListOf(data.mediaId),
                            name = name,
                            coverId = data.mediaId,
                            count = 1,
                            mediaType = 1
                        )
                    )
                }
            }
        }

        mainMediaItemMap.putAll(subMediaItemMap)

        val result = mutableListOf<SearchMediaItem>()
        mainMediaItemMap.values.forEach {
            result.add(it)
        }
        return result
    }

    fun dataFilter(dataList: List<MediaPath>, whiteList: List<MediaPath>): List<MediaPath> {
        return dataFilterById(dataList, whiteList.map {
            it.mediaId
        })
    }

    fun dataFilterById(dataList: List<MediaPath>, whiteList: List<Int>): List<MediaPath> {
        return dataList.filter {
            !whiteList.contains(it.mediaId)
        }
    }

    /**
     * 相同type的合在一起，每个type中过滤相同的mediaId
     */
    fun dataFoldByType(data: List<SearchMediaItem>): List<SearchMediaItem> {
        val itemTypeMap = LinkedHashMap<Int, SearchMediaItem>()
        data.forEach {
            if (itemTypeMap.containsKey(it.type)) {
                val mediaItem = itemTypeMap[it.type]
                mediaItem?.mediaIds?.run {
                    for (mediaId in it.mediaIds) {
                        if (!contains(mediaId)) {
                            add(mediaId)
                        }
                    }
                }
            } else {
                itemTypeMap[it.type] = it
            }
        }

        val result = mutableListOf<SearchMediaItem>()
        itemTypeMap.values.forEach {
            result.add(it)
        }
        return result
    }

    /**
     * 相同SearchResultKey(type Int, name String)的合在一起，每个type中过滤相同的mediaId
     */
    @JvmStatic
    fun dataFoldBySearchResultKey(data: List<SearchMediaItem>): List<SearchMediaItem> {
        val mediaItemMap = LinkedHashMap<SearchResultKey, SearchMediaItem>()
        mediaItemMap.putAndCombAll(data)

        val result = mutableListOf<SearchMediaItem>()
        mediaItemMap.values.forEach {
            result.add(it)
        }
        return result
    }

    fun mediaItemListFilter(dataList: List<SearchMediaItem>, whiteList: List<Int>): List<SearchMediaItem> {
        val foldedData = dataFoldBySearchResultKey(dataList)
        foldedData.forEach {
            it.mediaIds.retainAll(whiteList)
        }
        return foldedData
    }

}