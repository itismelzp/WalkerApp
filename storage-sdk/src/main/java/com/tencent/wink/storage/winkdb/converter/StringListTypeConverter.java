package com.tencent.wink.storage.winkdb.converter;

import androidx.annotation.Nullable;
import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by walkerzpli on 2021/9/22.
 */
public class StringListTypeConverter implements IListTypeConverter<String> {

    @TypeConverter
    @Nullable
    @Override
    public String converter(@Nullable List<String> list) {
        return new Gson().toJson(list);
    }

    @TypeConverter
    @Nullable
    @Override
    public List<String> revert(@Nullable String str) {
        Type type = new TypeToken<List<String>>() {
        }.getType();
        return new Gson().fromJson(str, type);
    }

}
