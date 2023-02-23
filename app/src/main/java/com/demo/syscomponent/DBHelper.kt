package com.demo.syscomponent

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Created by lizhiping on 2023/2/22.
 * <p>
 * description
 */
class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION, ) {

    companion object {

        const val DATABASE_NAME = "finch.db"

        const val USER_TABLE_NAME = "user"
        const val JOB_TABLE_NAME = "job"

        const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE IF NOT EXISTS $USER_TABLE_NAME(_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS $JOB_TABLE_NAME(_id INTEGER PRIMARY KEY AUTOINCREMENT, job TEXT)");
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

}

