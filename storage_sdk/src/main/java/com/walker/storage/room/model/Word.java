package com.walker.storage.room.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Created by walkerzpli on 2021/7/30.
 */

@Entity(tableName = "word_table")
public class Word {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "content")
    private String mContent;

    @ColumnInfo(name = "createTime")
    private long mCreateTime;


    public Word(@NonNull String content) {
        this.mContent = content;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return mContent;
    }

    public long getCreateTime() {
        return mCreateTime;
    }

    public void setCreateTime(long createTime) {
        this.mCreateTime = createTime;
    }


    @Override
    public String toString() {
        return "Word{" +
                "id=" + id +
                ", mContent='" + mContent + '\'' +
                ", mCreateTime=" + mCreateTime +
                '}';
    }
}
