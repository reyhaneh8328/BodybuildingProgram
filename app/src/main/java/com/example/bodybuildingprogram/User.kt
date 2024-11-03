package com.example.bodybuildingprogram

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "users")
 class User
{
    @PrimaryKey(autoGenerate = true)
    private var userId: Int = 0
    @ColumnInfo(name = "first_name")
    private var firstName: String = ""
    @ColumnInfo(name = "last_name")
    private var lastName: String = ""
    @ColumnInfo(name = "age")
    private var age: Int = 0
    @ColumnInfo(name = "height")
    private var height: Int = 0
    @ColumnInfo(name = "weight")
    private var weight: Int = 0
    @ColumnInfo(name = "desired")
    private var desired: String = ""
    @ColumnInfo(name = "blood_type")
    private var bloodType: String = ""

    constructor(
        firstName: String,
        lastName: String,
        age: Int,
        height: Int,
        weight: Int,
        desired: String,
        bloodType: String
    ) {
        this.firstName = firstName
        this.lastName = lastName
        this.age = age
        this.height = height
        this.weight = weight
        this.desired = desired
        this.bloodType = bloodType
    }

    fun getUserId() : Int{
        return this.userId
    }
    fun setUserId(userId: Int){
        this.userId = userId
    }
    fun getFirstName(): String{
        return this.firstName
    }
    fun setFirstName(firstName: String){
        this.firstName = firstName
    }
    fun getLastName(): String{
        return this.lastName
    }
    fun setLastName(lastName: String){
        this.lastName = lastName
    }
    fun getAge(): Int{
        return this.age
    }
    fun setAge(age: Int){
        this.age = age
    }
    fun getHeight(): Int{
        return this.height
    }
    fun setHeight(height: Int){
        this.height = height
    }
    fun getWeight(): Int{
        return this.weight
    }
    fun setWeight(weight: Int){
        this.weight = weight
    }
    fun getDesired(): String{
        return this.desired
    }
    fun setDesired(desired: String){
        this.desired = desired
    }
    fun getBloodType(): String{
        return this.bloodType
    }
    fun setBloodType(bloodType: String){
        this.bloodType = bloodType
    }
}