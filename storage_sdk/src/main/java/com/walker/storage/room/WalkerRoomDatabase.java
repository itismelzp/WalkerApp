package com.walker.storage.room;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.walker.storage.room.dao.LibraryDao;
import com.walker.storage.room.dao.MusicListDao;
import com.walker.storage.room.dao.UserDao;
import com.walker.storage.room.dao.WordDao;
import com.walker.storage.room.model.Address;
import com.walker.storage.room.model.Department;
import com.walker.storage.room.model.Library;
import com.walker.storage.room.model.MusicList;
import com.walker.storage.room.model.User;
import com.walker.storage.room.model.Word;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by walkerzpli on 2021/8/5.
 */

@Database(entities = {Word.class, User.class, Library.class, MusicList.class}, version = 1, exportSchema = false)
public abstract class WalkerRoomDatabase extends RoomDatabase {

    private static final String TAG = "WordRoomDatabase";

    public abstract WordDao wordDao();
    public abstract UserDao userDao();
    public abstract LibraryDao libraryDao();
    public abstract MusicListDao musicListDao();

    private static volatile WalkerRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREAD = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREAD);

    public static WalkerRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (WalkerRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            WalkerRoomDatabase.class, "word_database")
                            .addCallback(sRoomDatabaseCallback) // 数据库创建的时候回调
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            Log.d(TAG, "[onCreate] SupportSQLiteDatabase attachedDbs: " + db.getAttachedDbs());

            databaseWriteExecutor.execute(() -> {
                WordDao dao = INSTANCE.wordDao();
                dao.deleteAll();

                Word word = new Word("Hello");
                Address address = new Address("10086");
                address.street = "兴华路";
                address.state = "广东省";
                address.city = "深圳";

                dao.insert(word);

                word = new Word("World");
                dao.insert(word);

                initUsers();
            });
        }
    };

    private static void initUsers() {

        UserDao userDao = INSTANCE.userDao();
        LibraryDao libraryDao = INSTANCE.libraryDao();
        MusicListDao musicListDao = INSTANCE.musicListDao();

        // init users
        User user1 = new User();
        user1.firstName = "walker";
        user1.lastName = "lee";
        Address address1 = new Address("424500");
        address1.street = "粤海街道";
        address1.state = "广东省";
        address1.city = "深圳市";
        user1.address = address1;
        Department department1 = new Department();
        department1.id = 20001;
        department1.name = "pony";
        user1.department = department1;
        user1.jobs = Arrays.asList("baidu", "tencent");

        User user2 = new User();
        user2.firstName = "cherry";
        user2.lastName = "yan";
        Address address2 = new Address("424501");
        address2.street = "岳麓街道";
        address2.state = "湖南省";
        address2.city = "长沙市";
        user2.address = address2;

        userDao.insertAll(user1, user2);

        // init library
        Library library1 = new Library();
        library1.libraryID = 10086L;
        library1.userID = 1;
        libraryDao.insert(library1);

        Library library2 = new Library();
        library2.libraryID = 10087L;
        library2.userID = 1;
        libraryDao.insert(library2);

        // init music list
        MusicList musicList1 = new MusicList();
        musicList1.id = 100001;
        musicList1.listName = "jack zhou";
        musicList1.userID = 2;

        MusicList musicList2 = new MusicList();
        musicList2.id = 100002;
        musicList2.listName = " zhou";
        musicList2.userID = 2;

        musicListDao.insert(musicList1, musicList2);

    }

}
