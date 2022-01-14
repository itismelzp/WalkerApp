package com.walker.storage.winkdb.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.walker.storage.winkdb.model.MusicList;

import java.util.List;


/**
 * Created by walkerzpli on 2021/9/22.
 */
@Dao
public interface MusicListDao {

    @Insert
    void insert(MusicList... musicLists);

    @Query("SELECT * FROM music_list")
    LiveData<List<MusicList>> getAll();
}
