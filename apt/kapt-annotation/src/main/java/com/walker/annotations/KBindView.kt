package com.walker.annotations

/**
 * 用来注入view
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.BINARY)
annotation class KBindView(
    val value: Int
)
