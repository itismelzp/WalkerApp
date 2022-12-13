package com.walker.kapt

/**
 * 空类型
 */
class AutelEmptyConvert : IAutelConverter<Void, Void> {

    fun unpack(message: Any?): Void? {
        return null
    }

}