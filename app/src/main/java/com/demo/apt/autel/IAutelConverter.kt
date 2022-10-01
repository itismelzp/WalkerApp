package com.demo.apt.autel

import com.google.protobuf.*
import com.google.protobuf.Any
import kotlin.String

/**
 * @date 2022/9/20.
 * @author maowei
 * @description 接口层（外层）对象和协议层（底层）对象转换器
 */
abstract class IAutelConverter<T, D : GeneratedMessageV3> {

    protected var value: D? = null

    /**
     * 参数数据类型(指协议层约定的数据类型）
     */
    open fun getParameterMessageType(): DroneParameterType {
        return DroneParameterType.UNKOWN
    }

    /**
     * 设置接口层实体对象,生成协议层对象
     */
    open fun setValueFromBean(bean: T) {

    }

    open fun getBeanFromMessage(bean: D): T? {
        return null
    }


    /**
     * 对象转json，主要用于调试工具
     */
    open fun getJsonStr(): String? {
        return null
    }

    /**
     * json转对象，主要用于调试工具
     */
    open fun fromJsonStr(str: String): T? {
        return null
    }

    /**
     * 把接口层对象转成协议层对象
     */
    open fun pack(bean: T): Any? {
        setValueFromBean(bean)
        return if (value != null) AnyUtil.packMessage(value!!) else null
    }

    /**
     * 已废弃，后面需要删掉
     */
    open fun unpack(message: AutelMessage?): T? {
        return null
    }

    /**
     * 把协议层对象转成接口层对象
     */
    open fun unpack(message: Any?): T? {
        return null
    }

}