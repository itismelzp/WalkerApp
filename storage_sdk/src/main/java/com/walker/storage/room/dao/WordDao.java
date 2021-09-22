package com.walker.storage.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.walker.storage.room.model.Word;

import java.util.List;

/**
 * Created by walkerzpli on 2021/7/30.
 */
@Dao
public interface WordDao {

    // allowing the insert of the same word multiple times by passing a
    // conflict resolution strategy
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Word word);

//    @Delete
    @Query("DELETE FROM word_table")
    void deleteAll();

    @Query("DELETE FROM word_table WHERE content = :arg0")
    void delete(String arg0);

    @Query("SELECT * FROM word_table ORDER BY content ASC")
    LiveData<List<Word>> getAlphabetizedWords();

}
