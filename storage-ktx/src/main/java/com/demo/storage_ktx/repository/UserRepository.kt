package com.demo.storage_ktx.repository

import androidx.lifecycle.LiveData
import com.demo.storage_ktx.dao.UserDao
import com.demo.storage_ktx.model.User

/**
 * Created by walkerzpli on 2021/9/22.
 */
class UserRepository(private val mUserDao: UserDao) {

    private val mAllUsers: LiveData<List<User>> = mUserDao.getAll()

    fun getUser(id: Int): LiveData<User> {
        return mUserDao.getUser(id)
    }

    val allUsers: LiveData<List<User>>
        get() = mAllUsers

    suspend fun insert(user: User) {
        mUserDao.insert(user)
    }

    suspend fun insertAll(users: List<User>) {
        mUserDao.insertAll(users)
    }

    suspend fun delete(firstName: String) {
        mUserDao.delete(firstName)
    }

    suspend fun deleteAll() {
        mUserDao.deleteAll()
    }

    suspend fun update(user: User) {
        mUserDao.update(user)
    }
}