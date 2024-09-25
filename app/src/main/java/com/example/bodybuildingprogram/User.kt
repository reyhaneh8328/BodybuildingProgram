package com.example.bodybuildingprogram

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "users")
data class User (
    @PrimaryKey(autoGenerate = true)
    private val userId: Int = 0,
    @ColumnInfo(name = "first_name")
    private val firstName: String,
    @ColumnInfo(name = "last_name")
    private val lastName: String,
    @ColumnInfo(name = "age")
    private val age: Int,
    @ColumnInfo(name = "height")
    private val height: Int,
    @ColumnInfo(name = "weight")
    private val weight: Int,
    @ColumnInfo(name = "blood_type")
    private val bloodType: String
)