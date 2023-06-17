package com.demo.storage_ktx.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.demo.storage_ktx.model.User

/**
 * Created by walkerzpli on 2021/9/22.
 */
@Dao
interface UserDao {

    @Insert
    suspend fun insert(user: User)

    @Insert
    suspend fun insertAll(users: List<User>)

    @Query("DELETE FROM users WHERE first_name =:firstName")
    suspend fun delete(firstName: String)

    @Query("DELETE FROM users")
    suspend fun deleteAll()

    @Update
    suspend fun update(vararg users: User)

    @Query("SELECT * FROM users WHERE id =:id")
    fun getUser(id: Int): LiveData<User>

    @Query("SELECT * FROM users WHERE id =:id")
    fun getSingleUser(id: Int): User

    @Query("SELECT * FROM users")
    fun getAll(): LiveData<List<User>>

}