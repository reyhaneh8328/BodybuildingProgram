package com.example.bodybuildingprogram

class ModelTable {
    private val namePlan: String
    private val setNumber: String
    private val sizeSet: String
    private val description: String

    constructor(namePlan: String, setNumber: String, sizeSet: String, description: String) {
        this.namePlan = namePlan
        this.setNumber = setNumber
        this.sizeSet = sizeSet
        this.description = description
    }

    fun getNamePlan(): String{
        return this.namePlan
    }
    fun getSetNumber(): String{
        return this.setNumber
    }
    fun getSizeSet(): String{
        return this.sizeSet
    }
    fun getDescription(): String{
        return this.description
    }
}