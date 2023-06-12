package com.demo.storage_ktx

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.demo.storage_ktx.dao.UserDao
import com.demo.storage_ktx.dao.WordDao
import com.demo.storage_ktx.model.User
import com.demo.storage_ktx.model.Word
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Created by lizhiping on 2023/6/5.
 * <p>
 * description
 */
@Database(entities = [Word::class, User::class], version = 1, exportSchema = false)
abstract class WordRoomDatabase : RoomDatabase() {

    abstract fun wordDao(): WordDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: WordRoomDatabase? = null

        fun getDatabase(
            context: Context, scope: CoroutineScope
        ): WordRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    WordRoomDatabase::class.java,
                    "word_database_kt.db"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(WordDatabaseCallback(scope))
                    .build()
                    .also {
                        INSTANCE = it
                    }
            }
        }

        private class WordDatabaseCallback(
            private val scope: CoroutineScope
        ) : Callback() {

            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch {
                        populateDatabase(database.wordDao())
                    }
                }
            }

            suspend fun populateDatabase(wordDao: WordDao) {
                wordDao.deleteAll()
                val word1 = Word(0, "Hello")
                wordDao.insert(word1)
                val word2 = Word(1, "World!")
                wordDao.insert(word2)
            }
        }
    }

}
