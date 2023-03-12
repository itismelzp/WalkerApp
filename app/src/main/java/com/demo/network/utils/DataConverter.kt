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
}