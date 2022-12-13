package com.walker.kapt.compiler

import com.squareup.kotlinpoet.ClassName
import javax.lang.model.element.Element

/**
 * Created by lizhiping on 2022/10/14.
 * <p>
 * description
 */
data class AutelKeyInfo(
    var element: Element, // 这里用于拿到属性变量
    val keyName: String,
    var canSet: Boolean = false,
    var canGet: Boolean = false,
    var canAction: Boolean = false,
    var canListen: Boolean = false,
    var paramBean: ClassName,
    var paramMsg: String = "",
    var resultBean: ClassName,
    var resultMsg: String = "",
)
