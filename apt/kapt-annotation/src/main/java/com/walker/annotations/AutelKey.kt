package com.walker.annotations

import kotlin.reflect.KClass

/**
 * Created by lizhiping on 2022/10/1.
 * <p>
 * description
 */

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.BINARY)
annotation class AutelKey(
    val keyType: String,
    val keyName: String,
    val componentType: ComponentType,
    val canSet: Boolean = false,
    val canGet: Boolean = false,
    val canAction: Boolean = false,
    val canListen: Boolean = false,
    val paramBean: KClass<out Any>,
    val paramMsg: String = "",
    val resultBean: KClass<out Any>,
    val resultMsg: String = ""
)
