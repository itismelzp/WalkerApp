package com.demo.storage_ktx.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.demo.storage_ktx.model.User
import com.demo.storage_ktx.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Created by walkerzpli on 2021/9/22.
 */
class UserViewModel(private val mRepository: UserRepository) : ViewModel() {

    private val mAllUsers: LiveData<List<User>> = mRepository.allUsers

    fun getUser(id: Int): LiveData<User> {
        return mRepository.getUser(id)
    }

    val allUsers: LiveData<List<User>>
        get() = mAllUsers

    fun insert(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.insert(user)
        }
    }

    fun insert(users: List<User>) {
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.insertAll(users)
        }
    }

    fun delete(firstName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            mRepository.delete(firstName)
        }
    }
}

@Suppress("UNCHECKED_CAST")
class UserViewModelFactory(private val repository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}