package com.walker.storage.room;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.walker.storage.room.model.Department;

import java.lang.reflect.Type;


/**
 * Created by walkerzpli on 2021/9/22.
 */
public class DepartmentConvert {

    @TypeConverter
    public Department revert(String jsonStr) {
        Type type = new TypeToken<Department>() {}.getType();
        return new Gson().fromJson(jsonStr, type);
    }

    @TypeConverter
    public String converter(Department department) {
        return new Gson().toJson(department);
    }

}