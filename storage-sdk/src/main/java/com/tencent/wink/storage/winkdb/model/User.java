package com.tencent.wink.storage.winkdb.model;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;


import com.tencent.wink.storage.winkdb.converter.DepartmentTypeConvert;
import com.tencent.wink.storage.winkdb.converter.JobMapTypeConverter;
import com.tencent.wink.storage.winkdb.converter.StringListTypeConverter;

import java.util.List;
import java.util.Map;

/**
 * Created by walkerzpli on 2021/9/10.
 */

@Entity(tableName = "users", indices = {
        @Index(name = "name", value = {"first_name", "last_name"}, unique = true)
})
@TypeConverters({/*DepartmentTypeConvert.class, */StringListTypeConverter.class, JobMapTypeConverter.class})
public class User {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "first_name")
    public String firstName;

    @ColumnInfo(name = "last_name")
    public String lastName;

    /**
     * 嵌套写法@Embedded
     */
    @Embedded
    public Address address;

    @TypeConverters(DepartmentTypeConvert.class)
    public Department department;

    public List<String> company;

    public Map<Integer, Job> jobs;

    @ColumnInfo(name = "create_time")
    public int createTime;

    @ColumnInfo(name = "test")
    public String test;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address=" + address +
                ", department=" + department +
                ", company=" + company +
                ", jobs=" + jobs +
                '}';
    }
}
