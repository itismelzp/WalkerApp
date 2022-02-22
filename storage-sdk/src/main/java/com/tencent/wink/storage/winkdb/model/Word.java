package com.tencent.wink.storage.winkdb.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Objects;

/**
 * Created by walkerzpli on 2021/7/30.
 */

//@Entity(tableName = "word_table")
@Entity(tableName = "word_table", indices = {@Index(value = {"content"})})
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
        this.mCreateTime = System.currentTimeMillis();
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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Word word = (Word) o;
        return mCreateTime == word.mCreateTime && mContent.equals(word.mContent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mContent, mCreateTime);
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
