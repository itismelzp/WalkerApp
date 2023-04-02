package com.demo.utils

import kotlin.math.ceil

/**
 * Created by lizhiping on 2023/4/2.
 * <p>
 * description
 */


fun <E> List<E>.sliceByIndex(bucketSize: Int): Map<Int, List<E>> {
    val threadSize = ceil(size / bucketSize.toDouble()).toInt()
    var start: Int
    var end: Int
    val map = mutableMapOf<Int, List<E>>()
    for (i in 0 until threadSize) {
        start = i * bucketSize
        end = (i + 1) * bucketSize - 1
        if (end > size - 1) {
            end = size - 1
        }
        val slice = slice(start..end)
        map[i] = slice
    }
    return map
}

fun <E> List<E>.sliceByIndex(bucketSize: Int, handler: (Int, Int, List<E>) -> Unit) {
    sliceByIndex(bucketSize).forEach {
        handler(it.key, sliceByIndex(bucketSize).size, it.value)
    }
}
