package com.demo.kapt

import com.google.protobuf.Any
import com.google.protobuf.Internal
import com.google.protobuf.Message

fun <T : Message?> Any.unpackMessage(clazz: Class<T>): T? {
    return try {
        val defaultInstance: T = Internal.getDefaultInstance(clazz)
        defaultInstance?.parserForType?.parseFrom(this.value) as T
    } catch (e: Exception) {
        null
    }
}
