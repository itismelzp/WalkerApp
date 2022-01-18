package com.walker.storage.winkdb.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.walker.storage.winkdb.model.User;
import com.walker.storage.winkdb.relation.UserAndLibrary;
import com.walker.storage.winkdb.relation.UserWithMusicLists;

import java.util.List;


/**
 * Created by walkerzpli on 2021/9/22.
 */
@Dao
public interface UserDao {

    @Insert
    void insertAll(User... users);

    @Insert
    void insertAll(List<User> users);

    @Delete
    void deleteById(User user);

    @Update
    void update(User... users);

    @Query("SELECT * FROM users WHERE id =:id")
    LiveData<User> getUser(int id);

    @Query("SELECT * FROM users")
    LiveData<List<User>> getAll();

    @Transaction
    @Query("SELECT * FROM users")
    LiveData<List<UserAndLibrary>> getAllUserWithLibrary();

    @Transaction
    @Query("SELECT * FROM users")
    LiveData<List<UserWithMusicLists>> getAllUsersWithMusicLists();

}
