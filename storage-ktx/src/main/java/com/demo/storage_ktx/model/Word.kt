package com.demo.storage_ktx.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.demo.storage_ktx.Constants

/**
 * Created by lizhiping on 2023/6/5.
 * <p>
 * description
 */
@Entity(tableName = Constants.TABLE_WORD)
data class Word(
    @PrimaryKey val id: Int? = null,
    @ColumnInfo(name = "word") val word: String
)
