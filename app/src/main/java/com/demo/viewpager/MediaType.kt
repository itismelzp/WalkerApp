package com.demo.viewpager


/**
 * Created by lizhiping on 2023/2/2.
 *
 *
 * description
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.FIELD)
annotation class MediaType {
    companion object {
        var IMAGE = 1
        var VIDEO = 2
    }
}