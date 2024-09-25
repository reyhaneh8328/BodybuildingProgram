package com.example.bodybuildingprogram

import androidx.room.Room

class DatabaseConnection private constructor(){
    companion object {
        @Volatile private var instance: AppDatabase? = null

        fun getInstance(): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase().also { instance = it }
            }
        }

        private fun buildDatabase(): AppDatabase {
            return Room.databaseBuilder(
                MyApplication.applicationContext(), // دسترسی به applicationContext
                AppDatabase::class.java, "am_schema"
            ).build()
        }
    }
}