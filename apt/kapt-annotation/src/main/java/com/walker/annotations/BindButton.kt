package com.walker.annotations

import kotlin.reflect.KClass

@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.FIELD)
annotation class BindButton(val resId: Int,
                            val clazz: KClass<*>,
                            val flag: Int = -1)