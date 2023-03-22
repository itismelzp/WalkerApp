package com.demo.utils

/**
 * Created by lizhiping on 2023/3/21.
 * <p>
 * description
 */
object DataFactory {

    @JvmStatic
    fun getOdd(cnt: Int): List<Int> {
        return getData(1, cnt)
    }

    // 偶数
    @JvmStatic
    fun getEven(cnt: Int): List<Int> {
        return getData(0, cnt)
    }

    private fun getData(initial: Int, cnt: Int): List<Int> {
        val result = mutableListOf<Int>()
        var i = initial
        repeat(cnt) {
            result.add(i)
            i+=2
        }
        return result
    }
}