package com.walker.storage.room.model;

import androidx.room.ColumnInfo;
import androidx.room.Ignore;

/**
 * Created by walkerzpli on 2021/9/22.
 */
public class Department {

    @ColumnInfo(name = "_id")
    public int id;

    @ColumnInfo(name = "_name")
    public String name;

    public Department() {
    }

    @Ignore
    public Department(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Department{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
