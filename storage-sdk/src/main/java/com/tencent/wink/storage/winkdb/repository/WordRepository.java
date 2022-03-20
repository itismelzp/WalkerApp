package com.tencent.wink.storage.winkdb.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.tencent.wink.storage.winkdb.WinkRoomDatabase;
import com.tencent.wink.storage.winkdb.dao.WordDao;
import com.tencent.wink.storage.winkdb.model.Word;
import com.tencent.wink.storage.winkdb.log.WinkDbLog;

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
        WinkRoomDatabase db = WinkRoomDatabase.getDatabase(context);
        mWordDao = db.wordDao();
    }

    public LiveData<Word> getLastWord() {
        long begin = System.currentTimeMillis();
        LiveData<Word> lastWord = mWordDao.getLastWord();
        long cost = System.currentTimeMillis() - begin;
        WinkDbLog.i(TAG, "getLastWord finish, lastWord: " + lastWord + ", cost: " + cost);
        return lastWord;
    }

    public LiveData<List<Word>> getAllWords() {
        return mWordDao.getAlphabetizedWords();
    }

    public void insert(Word word) {
        WinkRoomDatabase.databaseWriteExecutor.execute(() -> {
            long begin = System.currentTimeMillis();
            mWordDao.insert(word);
            long cost = System.currentTimeMillis() - begin;
            WinkDbLog.i(TAG, "insert finish, word: " + word + ", cost: " + cost);
        });
    }

    public void insertAll(List<Word> words) {
        WinkRoomDatabase.databaseWriteExecutor.execute(() -> {
            long begin = System.currentTimeMillis();
            mWordDao.insertAll(words);
            long cost = System.currentTimeMillis() - begin;
            WinkDbLog.i(TAG, "insert finish, word: " + words + ", cost: " + cost);
        });
    }

    public void delete(String content) {
        WinkRoomDatabase.databaseWriteExecutor.execute(() -> {
            long begin = System.currentTimeMillis();
            mWordDao.delete(content);
            long cost = System.currentTimeMillis() - begin;
            WinkDbLog.i(TAG, "delete finish, content: " + content + ", cost: " + cost);
        });
    }

    public void delete(String... contents) {
        WinkRoomDatabase.databaseWriteExecutor.execute(() -> {
            long begin = System.currentTimeMillis();
            mWordDao.delete(contents);
            long cost = System.currentTimeMillis() - begin;
            WinkDbLog.i(TAG, "delete finish, contents: " + Arrays.toString(contents) + ", cost: " + cost);
        });
    }

    public void delete(List<String> contents) {
        WinkRoomDatabase.databaseWriteExecutor.execute(() -> {
            long begin = System.currentTimeMillis();
            mWordDao.delete(contents);
            long cost = System.currentTimeMillis() - begin;
            WinkDbLog.i(TAG, "delete finish, contents: " + contents + ", cost: " + cost);
        });
    }

    public void update(Word word) {
        WinkRoomDatabase.databaseWriteExecutor.execute(() -> {
            long begin = System.currentTimeMillis();
            mWordDao.update(word);
            long cost = System.currentTimeMillis() - begin;
            WinkDbLog.i(TAG, "delete finish, word: " + word + ", cost: " + cost);
        });
    }

}
