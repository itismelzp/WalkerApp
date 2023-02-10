package com.demo.datecenter

import com.demo.R

/**
 * Created by lizhiping on 2023/2/10.
 * <p>
 * description
 */
class ImageDataProvider {

    companion object {

        private var IMAGES = listOf(
            R.drawable.kpl_libai_01,
            R.drawable.kpl_libai_02,
            R.drawable.kpl_libai_03,
            R.drawable.kpl_libai_04,
            R.drawable.kpl_change_01,
            R.drawable.kpl_zhaoyun_01,
            R.drawable.kpl_zhaoyun_02,
            R.drawable.kpl_machao_01,
            R.drawable.kpl_machao_02,
            R.drawable.kpl_lvbu_01,
            R.drawable.kpl_lvbu_02
        )

        fun getImages(cnt: Int): List<Int> {
            val list = mutableListOf<Int>()
            if (cnt < 1) {
                return list
            }
            var count = cnt
            if (cnt > IMAGES.size) {
                count = IMAGES.size
            }

            for (i in 0 until count) {
                list.add(IMAGES[i])
            }
            return list
        }

        fun getRepeatableImages(cnt: Int): List<Int> {
            val list = mutableListOf<Int>()
            for (i in 0 until cnt) {
                list.add(IMAGES[i % IMAGES.size])
            }
            return list
        }

        fun getRepeatableImagesWithDelay(cnt: Int, dataCallback: DataCallback) {
            Thread.sleep(1000)

            dataCallback.onDataCompleted(getRepeatableImages(cnt))
        }

        fun getRepeatableImagesWithDelay(cnt: Int, millis: Long, dataCallback: DataCallback) {
            Thread.sleep(millis)
            dataCallback.onDataCompleted(getRepeatableImages(cnt))
        }

    }

    interface DataCallback {
        fun onDataCompleted(data: List<Int>)
    }

}