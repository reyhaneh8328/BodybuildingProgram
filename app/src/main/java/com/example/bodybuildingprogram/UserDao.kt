package com.example.bodybuildingprogram

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

interface UserDao {
    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<User>

    @Insert
    suspend fun insertUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)
}