package com.demo.storage_ktx.repository

import androidx.annotation.WorkerThread
import com.demo.storage_ktx.model.Word
import com.demo.storage_ktx.dao.WordDao
import kotlinx.coroutines.flow.Flow

/**
 * Created by lizhiping on 2023/6/5.
 * <p>
 * description
 */
class WordRepository(private val wordDao: WordDao) {

    val allWords: Flow<List<Word>> = wordDao.getAlphabetizedWords()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(word: Word) {
        wordDao.insert(word)
    }

    companion object {
        private const val TAG = "WordRepository"
    }
}