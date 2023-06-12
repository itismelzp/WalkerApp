package com.demo.storage_ktx.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.demo.storage_ktx.converter.StringListTypeConverter

/**
 * Created by walkerzpli on 2021/9/10.
 */
@Entity(
    tableName = "users",
    indices = [Index(name = "name", value = ["first_name", "last_name"], unique = false)]
)
@TypeConverters(StringListTypeConverter::class)
data class User(
    @PrimaryKey(autoGenerate = true)
    var id: Int?,

    @ColumnInfo(name = "first_name")
    var firstName: String?,

    @ColumnInfo(name = "last_name")
    var lastName: String?,

    @ColumnInfo(name = "company")
    var company: List<String>?,

    @ColumnInfo(name = "friends")
    var friends: List<String>?,

    @ColumnInfo(name = "create_time")
    var createTime: Long?,

    @ColumnInfo(name = "test")
    var test: String?
)