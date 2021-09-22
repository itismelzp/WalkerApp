package com.walker.storage.room.model;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.walker.storage.room.DepartmentConvert;
import com.walker.storage.room.ListConverter;

import java.util.List;

/**
 * Created by walkerzpli on 2021/9/10.
 */

@Entity(tableName = "users", indices = {
        @Index(name = "name", value = {"first_name", "last_name"}, unique = true)
})
@TypeConverters({DepartmentConvert.class, ListConverter.class})
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

//    @Embedded(prefix = "department")
    public Department department;

    public List<String> jobs;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address=" + address +
                ", department=" + department +
                '}';
    }
}
