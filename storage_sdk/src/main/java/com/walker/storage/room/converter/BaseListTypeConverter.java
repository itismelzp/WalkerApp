package com.walker.storage.room.converter;

import androidx.room.TypeConverter;


import java.util.List;

/**
 * Created by walkerzpli on 2021/9/22.
 */
public interface BaseListTypeConverter<T> {

    @TypeConverter
    String converter(List<T> list);

    @TypeConverter
    List<T> revert(String str);

}
