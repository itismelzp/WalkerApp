package com.walker.kthttp

/**
 * Created by lizhiping on 2023/4/23.
 * <p>
 * description
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class GET(val value: String)
