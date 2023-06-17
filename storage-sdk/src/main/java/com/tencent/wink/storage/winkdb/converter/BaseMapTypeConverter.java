package com.tencent.wink.storage.winkdb.converter;

import androidx.annotation.Nullable;
import androidx.room.TypeConverter;

import java.util.Map;

/**
 * Created by walkerzpli on 2021/9/22.
 */
public interface BaseMapTypeConverter<K, V> {

    @TypeConverter
    @Nullable
    String converter(@Nullable Map<K, V> data);

    @TypeConverter
    Map<K, V> revert(String str);

}
