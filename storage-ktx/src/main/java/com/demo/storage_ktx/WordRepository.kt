package com.demo.storage_ktx

import androidx.annotation.WorkerThread
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
}