package com.tencent.wink.storage.winkdb.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.tencent.wink.storage.winkdb.model.Word;

import java.util.List;

/**
 * Created by walkerzpli on 2021/7/30.
 */
@Dao
public interface WordDao {

    // allowing the insert of the same word multiple times by passing a
    // conflict resolution strategy
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Word word);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Word> words);

    @Query("DELETE FROM word_table")
    void deleteAll();

    @Query("DELETE FROM word_table WHERE content = :arg0")
    void delete(String arg0);

    @Query("DELETE FROM word_table WHERE content IN (:args)")
    void delete(String... args);

    @Query("DELETE FROM word_table WHERE content IN (:args)")
    void delete(List<String> args);

    @Query("DELETE FROM word_table WHERE content LIKE '%' || :arg || '%'")
    void deleteFuzzy(String arg);

    @Update
    void update(Word word);

    @Query("SELECT * FROM word_table ORDER BY content ASC")
    LiveData<List<Word>> getAlphabetizedWords();

    @Query("SELECT a.* FROM word_table a INNER JOIN (SELECT max(createTime) date FROM word_table) b ON a.createTime = b.date")
    LiveData<Word> getLastWord();

}
