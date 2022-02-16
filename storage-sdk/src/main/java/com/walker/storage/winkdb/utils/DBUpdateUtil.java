package com.walker.storage.winkdb.utils;

import androidx.annotation.NonNull;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by walkerzpli on 2022/1/24.
 */
public class DBUpdateUtil {

    private static final List<Migration> MIGRATION_LIST = new ArrayList<>();

    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // 执行升级相关操作
            database.execSQL("ALTER TABLE Users ADD COLUMN create_time INTEGER NOT NULL DEFAULT 0");
        }
    };

    public static void putMigration(Migration migration) {
        MIGRATION_LIST.add(migration);
    }

    @NonNull
    public static Migration[] getMigrations() {
        if (MIGRATION_LIST.size() == 0) {
            return new Migration[0];
        }
        Migration[] migrations = new Migration[MIGRATION_LIST.size()];
        MIGRATION_LIST.toArray(migrations);
        return migrations;
    }

}
