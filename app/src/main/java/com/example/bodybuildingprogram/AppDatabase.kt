package com.example.bodybuildingprogram

import androidx.room.RoomDatabase

abstract class AppDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
}