package com.walker.storage.winkdb.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.walker.storage.winkdb.WalkerRoomDatabase;
import com.walker.storage.winkdb.dao.WordDao;
import com.walker.storage.winkdb.model.Word;
import com.walker.storage.winkdb.utils.DbLogUtil;

import java.util.Arrays;
import java.util.List;

/**
 * Created by walkerzpli on 2021/8/5.
 */
public class WordRepository {

    private static final String TAG = "WordRepository";

    private WordDao mWordDao;
    private LiveData<List<Word>> mAllWords;

    public WordRepository(Context context) {
        WalkerRoomDatabase db = WalkerRoomDatabase.getDatabase(context);
        mWordDao = db.wordDao();
        mAllWords = mWordDao.getAlphabetizedWords();
    }

    public LiveData<Word> getLastWord() {
        return mWordDao.getLastWord();
    }

    public LiveData<List<Word>> getAllWords() {
        return mAllWords;
    }

    public void insert(Word word) {
        WalkerRoomDatabase.databaseWriteExecutor.execute(() -> {
//            mWordDao.delete(word.getContent());
            long begin = System.currentTimeMillis();
            mWordDao.insert(word);
            long cost = System.currentTimeMillis() - begin;
            DbLogUtil.i(TAG, "insert finish, word: " + word + ", cost: " + cost);
        });
    }

    public void delete(String content) {
        WalkerRoomDatabase.databaseWriteExecutor.execute(() -> {
            long begin = System.currentTimeMillis();
            mWordDao.delete(content);
            long cost = System.currentTimeMillis() - begin;
            DbLogUtil.i(TAG, "delete finish, content: " + content + ", cost: " + cost);
        });
    }

    public void delete(String... contents) {
        WalkerRoomDatabase.databaseWriteExecutor.execute(() -> {
            long begin = System.currentTimeMillis();
            mWordDao.delete(contents);
            long cost = System.currentTimeMillis() - begin;
            DbLogUtil.i(TAG, "delete finish, contents: " + Arrays.toString(contents) + ", cost: " + cost);
        });
    }

    public void delete(List<String> contents) {
        WalkerRoomDatabase.databaseWriteExecutor.execute(() -> {
            long begin = System.currentTimeMillis();
            mWordDao.delete(contents);
            long cost = System.currentTimeMillis() - begin;
            DbLogUtil.i(TAG, "delete finish, contents: " + contents + ", cost: " + cost);
        });
    }

    public void update(Word word) {
        WalkerRoomDatabase.databaseWriteExecutor.execute(() -> {
            long begin = System.currentTimeMillis();
            mWordDao.update(word);
            long cost = System.currentTimeMillis() - begin;
            DbLogUtil.i(TAG, "delete finish, word: " + word + ", cost: " + cost);
        });
    }

}
