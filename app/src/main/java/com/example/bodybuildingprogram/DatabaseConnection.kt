package com.example.bodybuildingprogram

import android.content.Context
import androidx.room.Room

class DatabaseConnection private constructor(){
    companion object {
        @Volatile private var instance: AppDatabase? = null

        fun getInstance(context:Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java, "am_schema"
            ).build()
        }
    }
}