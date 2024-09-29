package com.example.bodybuildingprogram

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Transaction
import kotlinx.coroutines.launch


class UserViewModel(private val userDao: UserDao) : ViewModel() {
    fun addUser(user: User) {
        viewModelScope.launch {
            userDao.insertUser(user)
        }
    }

    fun removeUser(user: User) {
        viewModelScope.launch {
            userDao.deleteUser(user)
        }
    }

    fun getUserById(userId: Int): LiveData<User> {
        return userDao.getUserById(userId)
    }

    fun getAllUsers(): LiveData<List<User>> {
        return userDao.getAllUsers()
    }
}