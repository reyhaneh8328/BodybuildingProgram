package com.example.bodybuildingprogram

import android.widget.TableRow
import android.widget.TextView

class ModelPlan {
    private val typePlan: String
    private val prePlan: String
    private val tableList: List<TableRow>
    private val time: Int
    private val intensity: Int
    private val restTime: Int
    private val pauseTime: Int

    constructor(
        typePlan: String,
        prePlan: String,
        tableList: List<TableRow>,
        time: Int,
        intensity: Int,
        restTime: Int,
        pauseTime: Int
    ) {
        this.typePlan = typePlan
        this.prePlan = prePlan
        this.tableList = tableList
        this.time = time
        this.intensity = intensity
        this.restTime = restTime
        this.pauseTime = pauseTime
    }
    fun getTypePlan(): String{
        return this.typePlan
    }
    fun getPrePlan(): String{
        return this.prePlan
    }
    fun getTableList(): List<TableRow>{
        return this.tableList
    }
    fun getTime(): Int{
        return this.time
    }
    fun getIntensity(): Int{
        return this.intensity
    }
    fun getRestTime(): Int{
        return this.restTime
    }
    fun getPauseTime(): Int{
        return this.pauseTime
    }

}