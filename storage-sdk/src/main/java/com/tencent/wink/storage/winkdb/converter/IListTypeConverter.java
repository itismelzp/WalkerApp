package com.tencent.wink.storage.winkdb.converter;

import androidx.annotation.Nullable;
import androidx.room.TypeConverter;



import java.util.List;

/**
 * Created by walkerzpli on 2021/9/22.
 */
public interface IListTypeConverter<T> {

    @TypeConverter
    @Nullable
    String converter(@Nullable List<T> list);

    @TypeConverter
    @Nullable
    List<T> revert(@Nullable String str);

}
