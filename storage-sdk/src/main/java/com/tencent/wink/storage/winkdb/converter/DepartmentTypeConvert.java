package com.tencent.wink.storage.winkdb.converter;

import androidx.annotation.Nullable;
import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.wink.storage.winkdb.model.Department;

import java.lang.reflect.Type;


/**
 * Created by walkerzpli on 2021/9/22.
 */
public class DepartmentTypeConvert {

    @TypeConverter
    @Nullable
    public Department revert(@Nullable String jsonStr) {
        Type type = new TypeToken<Department>() {}.getType();
        return new Gson().fromJson(jsonStr, type);
    }

    @TypeConverter
    @Nullable
    public String converter(@Nullable Department department) {
        return new Gson().toJson(department);
    }

}
