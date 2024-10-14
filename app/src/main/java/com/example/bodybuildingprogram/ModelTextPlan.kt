package com.example.bodybuildingprogram

class ModelTextPlan {
    private val name:String
    private val description: String

    constructor(name:String,description:String){
        this.name = name
        this.description = description
    }
    fun getName():String{
        return this.name
    }
    fun getDescription():String{
        return this.description
    }
}