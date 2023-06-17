package com.tencent.wink.storage.winkdb;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.tencent.wink.storage.winkdb.dao.MusicListDao;
import com.tencent.wink.storage.winkdb.dao.LibraryDao;
import com.tencent.wink.storage.winkdb.dao.UserDao;
import com.tencent.wink.storage.winkdb.dao.WordDao;
import com.tencent.wink.storage.winkdb.model.Library;
import com.tencent.wink.storage.winkdb.model.MusicList;
import com.tencent.wink.storage.winkdb.model.User;
import com.tencent.wink.storage.winkdb.model.Word;
import com.tencent.wink.storage.winkdb.utils.DBUpdateUtil;
import com.tencent.wink.storage.winkdb.safe.WinkDBHelperFactory;
import com.tencent.wink.storage.winkdb.utils.DataFactoryUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by walkerzpli on 2021/8/5.
 */

@Database(entities = {Word.class, User.class, Library.class, MusicList.class}, version = 5, exportSchema = false)
public abstract class WinkRoomDatabase extends RoomDatabase {

    private static final String TAG = "WordRoomDatabase";
    private static final char[] PASSPHRASE = {'w', 'i', 'n', 'k'};

    public abstract WordDao wordDao();

    public abstract UserDao userDao();

    public abstract LibraryDao libraryDao();

    public abstract MusicListDao musicListDao();

    private static volatile WinkRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREAD = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREAD);

    private static final WinkDBHelperFactory FACTORY = new WinkDBHelperFactory(PASSPHRASE);

    public static WinkRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (WinkRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            WinkRoomDatabase.class, "wink_database.db")
//                            .openHelperFactory(FACTORY)
                            .addCallback(sRoomDatabaseCallback) // 数据库创建的时候回调
                            .fallbackToDestructiveMigration()
                            .enableMultiInstanceInvalidation() // 使多实例失效，跨进程修改适用
                            .addMigrations(getMigrations())
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private final static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            Log.d(TAG, "[onCreate] SupportSQLiteDatabase attachedDbs: " + db.getAttachedDbs());

            databaseWriteExecutor.execute(() -> {
                DataFactoryUtil.initWords(INSTANCE);
                DataFactoryUtil.initUsers(INSTANCE);
            });
        }
    };

    @NonNull
    private static Migration[] getMigrations() {
        return DBUpdateUtil.getMigrations();
    }

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // 执行升级相关操作
            database.execSQL("ALTER TABLE Users ADD COLUMN create_time INTEGER NOT NULL DEFAULT 0");
        }
    };

    private static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // 执行升级相关操作
            database.execSQL("ALTER TABLE Users ADD COLUMN test TEXT");
        }
    };

}
