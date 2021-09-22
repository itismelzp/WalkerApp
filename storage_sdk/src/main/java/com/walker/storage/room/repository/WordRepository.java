package com.walker.storage.room.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.walker.storage.room.WalkerRoomDatabase;
import com.walker.storage.room.dao.WordDao;
import com.walker.storage.room.model.Word;

import java.util.List;

/**
 * Created by walkerzpli on 2021/8/5.
 */
public class WordRepository {

    private WordDao mWordDao;
    private LiveData<List<Word>> mAllWords;

    public WordRepository(Application application) {
        WalkerRoomDatabase db = WalkerRoomDatabase.getDatabase(application);
        mWordDao = db.wordDao();
        mAllWords = mWordDao.getAlphabetizedWords();
    }

    public LiveData<List<Word>> getAllWords() {
        return mAllWords;
    }

    public void insert(Word word) {
        WalkerRoomDatabase.databaseWriteExecutor.execute(() -> mWordDao.insert(word));
    }

    public void delete(String content) {
        WalkerRoomDatabase.databaseWriteExecutor.execute(() -> mWordDao.delete(content));
    }
}
