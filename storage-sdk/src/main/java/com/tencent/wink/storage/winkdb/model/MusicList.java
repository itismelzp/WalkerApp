package com.tencent.wink.storage.winkdb.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Created by walkerzpli on 2021/9/22.
 */
@Entity(tableName = "music_list")
public class MusicList {

    @PrimaryKey
    public long id;

    @ColumnInfo(name = "list_name")
    public String listName;

    @ColumnInfo(name = "user_id")
    public long userID;

    @Override
    public String toString() {
        return "MusicList{" +
                "id=" + id +
                ", listName='" + listName + '\'' +
                ", userID=" + userID +
                '}';
    }
}
