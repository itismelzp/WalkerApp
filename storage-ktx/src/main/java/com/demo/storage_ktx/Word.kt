package com.demo.storage_ktx

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by lizhiping on 2023/6/5.
 * <p>
 * description
 */
@Entity(tableName = "word_table")
data class Word(
    @PrimaryKey val id: Int? = null,
    @ColumnInfo(name = "word") val word: String
)
