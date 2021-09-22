package com.walker.storage.room.converter;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.walker.storage.room.model.Job;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by walkerzpli on 2021/9/22.
 */
public class JobMapTypeConverter implements BaseMapTypeConverter<Integer, Job> {

    @TypeConverter
    @Override
    public String converter(Map<Integer, Job> data) {
        return new Gson().toJson(data);
    }

    @TypeConverter
    @Override
    public Map<Integer, Job> revert(String str) {
        Type type = new TypeToken<Map<Integer, Job>>() {}.getType();
        return new Gson().fromJson(str, type);
    }

}
