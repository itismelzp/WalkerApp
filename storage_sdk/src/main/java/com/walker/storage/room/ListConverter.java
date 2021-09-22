package com.walker.storage.room;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by walkerzpli on 2021/9/22.
 */
public class ListConverter {

    @TypeConverter
    public String list2Str(List<String> list) {
        return new Gson().toJson(list);
    }

    @TypeConverter
    public List<String> str2List(String str) {
        Type type = new TypeToken<List<String>>() {}.getType();
        return new Gson().fromJson(str, type);
    }

}
