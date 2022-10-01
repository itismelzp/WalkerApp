package com.demo.apt.autel

import com.google.protobuf.Any
import com.google.protobuf.Internal
import com.google.protobuf.Message

/**
 * @date 2022/9/30.
 * @author maowei
 * @description Proto Any的扩展方法
 */

/**
 * 把Message转成protobuf的any
 */


/**
 * 把protobuf的any转成Message
 */
fun <T : Message?> Any.unpackMessage(clazz: Class<T>): T? {
    return try {
        val defaultInstance: T = Internal.getDefaultInstance(clazz)
        defaultInstance?.parserForType?.parseFrom(this.value) as T
    } catch (e: Exception) {
        null
    }
}

object AnyUtil {

    fun <T : Message> packMessage(message: T): Any {
        return Any.newBuilder()
            .setValue(message.toByteString())
            .build()
    }

    fun <T : Message?> unpackMessage(data: Any, clazz: Class<T>): T? {
        return try {
            val defaultInstance: T = Internal.getDefaultInstance(clazz)
            defaultInstance?.parserForType?.parseFrom(data.value) as T
        } catch (e: Exception) {
            null
        }
    }
}

