package com.walker.storage.winkdb.utils;

import androidx.annotation.NonNull;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

/**
 * Created by walkerzpli on 2022/1/24.
 */
public class DBUpdateUtil {

    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // 执行升级相关操作
            database.execSQL("ALTER TABLE Users ADD COLUMN create_time INTEGER NOT NULL DEFAULT 0");
        }
    };

}
