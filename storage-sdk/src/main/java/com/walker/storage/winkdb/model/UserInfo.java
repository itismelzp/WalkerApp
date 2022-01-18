package com.walker.storage.winkdb.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

/**
 * Created by walkerzpli on 2021/9/22.
 */
@Entity(tableName = "UserInfo", foreignKeys = @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "user_id"))
public class UserInfo {

    @PrimaryKey
    public int user_id;

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "sex")
    public String sex;

    @ColumnInfo(name = "age")
    public int age;
}
