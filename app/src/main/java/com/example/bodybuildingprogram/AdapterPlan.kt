package com.example.bodybuildingprogram

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bodybuildingprogram.databinding.RowTableplanBinding


class AdapterPlan: RecyclerView.Adapter<AdapterPlan.HolderPlan>{
    private val context: Context
    private var planArrayList: ArrayList<ModelPlan>

    private lateinit var binding: RowTableplanBinding

    constructor(context: Context, planArrayList: ArrayList<ModelPlan>) {
        this.context = context
        this.planArrayList = planArrayList
    }

    inner class HolderPlan(itemView: View): RecyclerView.ViewHolder(itemView){
        //init ui views
        var typePlanTv = binding.typePlanTv
        var prePlanTv = binding.prePlanTv
        var tableLl = binding.tableTl
        var infoPlanTv = binding.infoPlanTv
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderPlan {
        binding = RowTableplanBinding.inflate(LayoutInflater.from(context),parent, false)
        return HolderPlan(binding.root)
    }

    override fun getItemCount(): Int {
        return planArrayList.size
    }

    override fun onBindViewHolder(holder: HolderPlan, position: Int) {
        val model = planArrayList[position]

        holder.typePlanTv.text = model.getTypePlan()
        holder.prePlanTv.text = model.getPrePlan()
        for (i in model.getTableList().indices){
            holder.tableLl.addView(model.getTableList()[i])
        }
        holder.infoPlanTv.text = "مدت تمرین : ${model.getTime()} ساعت / شدت تمرین : ${model.getIntensity()}% /استراحت بین هر ست : ${model.getRestTime()} / مکث در حرکات : ${model.getPauseTime()}"
    }
}