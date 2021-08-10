package com.walker.storage.room;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Created by walkerzpli on 2021/7/30.
 */

@Entity(tableName = "word_table")
public class Word {

//    @PrimaryKey(autoGenerate = true)
//    private int id;

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "word")
    private String mWord;

    public Word(@NonNull String word) {
        this.mWord = word;
    }

    public String getWord() {
        return mWord;
    }
}
