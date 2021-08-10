package com.walker.storage.room;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

/**
 * Created by walkerzpli on 2021/8/5.
 */
class WordRepository {

    private WordDao mWordDao;
    private LiveData<List<Word>> mAllWords;

    WordRepository(Application application) {
        WordRoomDatabase db = WordRoomDatabase.getDatabase(application);
        mWordDao = db.wordDao();
        mAllWords = mWordDao.getAlphabetizedWords();
    }

    LiveData<List<Word>> getAllWords() {
        return mAllWords;
    }

    void insert(Word word) {
        WordRoomDatabase.databaseWriteExecutor.execute(() ->
                mWordDao.insert(word));
    }

}
