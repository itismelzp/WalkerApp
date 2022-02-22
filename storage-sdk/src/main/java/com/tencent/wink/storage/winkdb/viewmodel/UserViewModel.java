package com.tencent.wink.storage.winkdb.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.tencent.wink.storage.winkdb.model.User;
import com.tencent.wink.storage.winkdb.relation.UserAndLibrary;
import com.tencent.wink.storage.winkdb.relation.UserWithMusicLists;
import com.tencent.wink.storage.winkdb.repository.UserRepository;

import java.util.List;

/**
 * Created by walkerzpli on 2021/9/22.
 */
public class UserViewModel extends AndroidViewModel {

    private final UserRepository mRepository;
    private final LiveData<List<User>> mAllUsers;
    private final LiveData<List<UserAndLibrary>> mAllUsersAndLibrary;
    private final LiveData<List<UserWithMusicLists>> mAllUsersWithMusicLists;

    public UserViewModel(@NonNull Application application) {
        super(application);
        mRepository = new UserRepository(application);
        mAllUsers = mRepository.getAllUsers();
        mAllUsersAndLibrary = mRepository.getAllUsersAndLibrary();
        mAllUsersWithMusicLists = mRepository.getAllUsersAndMusicLists();
    }

    public LiveData<User> getUser(int id) {
        return mRepository.getUser(id);
    }

    public LiveData<List<User>> getAllUsers() {
        return mAllUsers;
    }

    public LiveData<List<UserAndLibrary>> getAllUsersWithLibrary() {
        return mAllUsersAndLibrary;
    }

    public LiveData<List<UserWithMusicLists>> getAllUsersWithMusicLists() {
        return mAllUsersWithMusicLists;
    }

    public void insert(User user) {
        mRepository.insert(user);
    }


    public void delete(User user) {
        mRepository.delete(user);
    }

}
