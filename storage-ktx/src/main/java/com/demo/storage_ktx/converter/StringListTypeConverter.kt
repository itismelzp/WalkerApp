package com.demo.storage_ktx.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Created by walkerzpli on 2021/9/22.
 */
class StringListTypeConverter : IListTypeConverter<String> {

    @TypeConverter
    override fun converter(list: List<String>?): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    override fun revert(str: String?): List<String> {
        val type = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(str, type)
    }
}