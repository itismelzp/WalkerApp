package com.walker.storage.winkdb.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.walker.storage.winkdb.model.Library;

import java.util.List;

/**
 * Created by walkerzpli on 2021/9/22.
 */
@Dao
public interface LibraryDao {

    @Insert
    void insert(Library... libraries);

    @Query("SELECT * FROM library")
    LiveData<List<Library>> getAll();
}
