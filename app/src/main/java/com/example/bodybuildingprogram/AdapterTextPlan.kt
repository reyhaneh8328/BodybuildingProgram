package com.example.bodybuildingprogram

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bodybuildingprogram.databinding.RowTextplanBinding

class AdapterTextPlan: RecyclerView.Adapter<AdapterTextPlan.HolderTextPlan> {
    private val context: Context
    private var textArrayList: ArrayList<ModelTextPlan>

    private lateinit var binding: RowTextplanBinding

    constructor(context: Context, textArrayList: ArrayList<ModelTextPlan>) {
        this.context = context
        this.textArrayList = textArrayList
    }

    inner class HolderTextPlan(itemView: View): RecyclerView.ViewHolder(itemView){
        //init ui views
        var nameSOVTv: TextView = binding.nameSOVTv
        var descriptionTv: TextView = binding.descriptionTv
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderTextPlan {
        binding = RowTextplanBinding.inflate(LayoutInflater.from(context),parent, false)
        return HolderTextPlan(binding.root)
    }

    override fun getItemCount(): Int {
        return textArrayList.size
    }

    override fun onBindViewHolder(holder: HolderTextPlan, position: Int) {
        val model = textArrayList[position]

        holder.nameSOVTv.text = model.getName()
        holder.descriptionTv.text = model.getDescription()
    }
}
