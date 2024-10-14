package com.example.bodybuildingprogram

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.bodybuildingprogram.databinding.RowTextplanBinding
import kotlin.reflect.typeOf

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

        holder.itemView.setOnLongClickListener { modelText->
            val builder = AlertDialog.Builder(context)
            builder.setTitle("حذف")
                .setMessage("آیا مطمئن هستید که می خواهید این مورد را حذف کنید؟")
                .setPositiveButton("تایید") { a, d ->
                    textArrayList.removeAt(position)
                    notifyDataSetChanged()
                    Toast.makeText(context, "حذف شد", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("لغو") { a, d ->
                    a.dismiss()
                }
                .show()
                .setCanceledOnTouchOutside(false)
            true
        }
    }
}
