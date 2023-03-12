package com.demo.network.utils

import com.demo.network.model.MediaItem
import com.demo.network.model.MediaPath

/**
 * Created by lizhiping on 2023/3/12.
 * <p>
 * description
 */
object DataConverter {

    fun dataConvert(dataList: List<MediaPath>): List<MediaItem> {
        val mediaItemMap = mutableMapOf<Int, MediaItem>()
        for (data in dataList) {
            data.type.forEachIndexed { index, type ->
                if (!mediaItemMap.containsKey(type)) {
                    val mediaItem = MediaItem(
                        data.dataToken,
                        mutableListOf(data.mediaId),
                        data.mediaPath,
                        data.name[index],
                        type
                    )
                    mediaItemMap[type] = mediaItem
                } else {
                    val mediaItem = mediaItemMap[type]
                    mediaItem?.mediaIds?.add(data.mediaId)
                }
            }
        }

        val result = mutableListOf<MediaItem>()
        mediaItemMap.values.forEach {
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
    fun dataFold(data: List<MediaItem>): List<MediaItem> {
        val itemTypeMap = LinkedHashMap<Int, MediaItem>()
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

        val result = mutableListOf<MediaItem>()
        itemTypeMap.values.forEach {
            result.add(it)
        }
        return result
    }

    fun mediaItemListFilter(dataList: List<MediaItem>, whiteList: List<Int>): List<MediaItem> {
        val foldedData = dataFold(dataList)
        foldedData.forEach {
            it.mediaIds.retainAll(whiteList)
        }
        return foldedData
    }

}