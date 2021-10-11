package com.walker.storage.room.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.walker.storage.room.WalkerRoomDatabase;
import com.walker.storage.room.dao.UserDao;
import com.walker.storage.room.model.User;
import com.walker.storage.room.relation.UserAndLibrary;
import com.walker.storage.room.relation.UserWithMusicLists;

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
        WalkerRoomDatabase db = WalkerRoomDatabase.getDatabase(application);
        mUserDao = db.userDao();
        mAllUsers = mUserDao.getAll();
        mAllUsersAndLibrary = mUserDao.getAllUserWithLibrary();
        mAllUsersAndMusicLists = mUserDao.getAllUsersWithMusicLists();
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
        WalkerRoomDatabase.databaseWriteExecutor.execute(() -> mUserDao.insertAll(user));
    }

    public void delete(User user) {
        WalkerRoomDatabase.databaseWriteExecutor.execute(() -> mUserDao.deleteById(user));
    }

    public void update(User... users) {
        WalkerRoomDatabase.databaseWriteExecutor.execute(() -> mUserDao.update(users));
    }

}
