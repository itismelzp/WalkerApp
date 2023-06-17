package com.tencent.wink.storage.winkdb.converter;

import androidx.annotation.Nullable;
import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.wink.storage.winkdb.model.Job;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by walkerzpli on 2021/9/22.
 */
public class JobMapTypeConverter implements BaseMapTypeConverter<Integer, Job> {

    @TypeConverter
    @Nullable
    @Override
    public String converter(@Nullable Map<Integer, Job> data) {
        return new Gson().toJson(data);
    }

    @TypeConverter
    @Nullable
    @Override
    public Map<Integer, Job> revert(@Nullable String str) {
        Type type = new TypeToken<Map<Integer, Job>>() {
        }.getType();
        return new Gson().fromJson(str, type);
    }

}
