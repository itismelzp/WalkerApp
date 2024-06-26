package com.walker.annotations

/**
 * 用来注入view
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.BINARY)
annotation class BindView(
    val value: Int
)
