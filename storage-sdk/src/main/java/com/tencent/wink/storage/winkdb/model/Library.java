package com.tencent.wink.storage.winkdb.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Created by walkerzpli on 2021/9/22.
 */
@Entity(tableName = "library")
public class Library {

    @PrimaryKey
    public long libraryID;

    @ColumnInfo(name = "user_id")
    public long userID;

    @Override
    public String toString() {
        return "Library{" +
                "libraryID=" + libraryID +
                ", userID=" + userID +
                '}';
    }
}
