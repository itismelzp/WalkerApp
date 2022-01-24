package com.walker.storage.winkdb.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.walker.storage.winkdb.WinkRoomDatabase;
import com.walker.storage.winkdb.dao.UserDao;
import com.walker.storage.winkdb.model.User;
import com.walker.storage.winkdb.relation.UserAndLibrary;
import com.walker.storage.winkdb.relation.UserWithMusicLists;

import java.util.List;

/**
 * Created by walkerzpli on 2021/9/22.
 */
public class UserRepository {

    private UserDao mUserDao;

    private LiveData<List<User>> mAllUsers;
    private LiveData<List<UserAndLibrary>> mAllUsersAndLibrary;
    private LiveData<List<UserWithMusicLists>> mAllUsersAndMusicLists;

    public UserRepository(Application application) {
        WinkRoomDatabase db = WinkRoomDatabase.getDatabase(application);
        mUserDao = db.userDao();
        mAllUsers = mUserDao.getAll();
        mAllUsersAndLibrary = mUserDao.getAllUserWithLibrary();
        mAllUsersAndMusicLists = mUserDao.getAllUsersWithMusicLists();
    }

    public LiveData<User> getUser(int id) {
        return mUserDao.getUser(id);
    }

    public LiveData<List<User>> getAllUsers() {
        return mAllUsers;
    }

    public LiveData<List<UserAndLibrary>> getAllUsersAndLibrary() {
        return mAllUsersAndLibrary;
    }

    public LiveData<List<UserWithMusicLists>> getAllUsersAndMusicLists() {
        return mAllUsersAndMusicLists;
    }

    public void insert(User user) {
        WinkRoomDatabase.databaseWriteExecutor.execute(() -> mUserDao.insertAll(user));
    }

    public void delete(User user) {
        WinkRoomDatabase.databaseWriteExecutor.execute(() -> mUserDao.delete(user));
    }

    public void update(User... users) {
        WinkRoomDatabase.databaseWriteExecutor.execute(() -> mUserDao.update(users));
    }

}
