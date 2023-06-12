package com.demo.storage_ktx.converter

import androidx.room.TypeConverter

/**
 * Created by walkerzpli on 2021/9/22.
 */
interface IListTypeConverter<T> {
    @TypeConverter
    fun converter(list: List<T>?): String?

    @TypeConverter
    fun revert(str: String?): List<T>
}