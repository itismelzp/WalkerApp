package com.walker.storage.winkdb.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Transaction;

import com.walker.storage.winkdb.model.WinkPublishContent;

/**
 * Created by walkerzpli on 2022/2/15.
 */
@Dao
public abstract class WinkPublishContentDao {
    @Insert
    public abstract void insert(WinkPublishContent product);

    @Delete
    public abstract void delete(WinkPublishContent product);

    @Transaction
    public void insertAndDeleteInTransaction(WinkPublishContent newData, WinkPublishContent oldData) {
        insert(newData);
        delete(oldData);
    }
}
