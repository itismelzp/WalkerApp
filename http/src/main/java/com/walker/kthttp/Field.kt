package com.walker.kthttp

/**
 * Created by lizhiping on 2023/4/23.
 * <p>
 * description
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Field(val value: String)
