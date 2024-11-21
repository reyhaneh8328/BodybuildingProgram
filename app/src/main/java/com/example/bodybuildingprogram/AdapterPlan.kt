package com.example.bodybuildingprogram

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    override fun onBindViewHolder(holder: HolderPlan, position: Int) {
    val model = planArrayList[position]

    // تنظیم متن‌های TextView
    holder.typePlanTv.text = "جلسه ${SessionNumber.entries[position]} / ${model.getTypePlan()}"
    holder.prePlanTv.text = model.getPrePlan()

    // اگر tableLl بیش از یک ویو دارد، فقط ویوهای بعدی را حذف کن
    while (holder.tableLl.childCount > 1) {
        holder.tableLl.removeViewAt(1) // حذف ویوهای بعد از اولین
    }

    // اضافه کردن ویوهای جدید به `tableLl` از ایندکس 1 به بعد
    for (i in model.getTableList().indices) {
        val tableItemView = model.getTableList()[i]

        // چک کردن اینکه آیا ویو قبلاً والد دارد
        if (tableItemView.parent != null) {
            (tableItemView.parent as ViewGroup).removeView(tableItemView)
        }

        // اضافه کردن ویو از ایندکس 1 به بعد
        holder.tableLl.addView(tableItemView)
    }

    // تنظیم متن اطلاعات دیگر
    holder.infoPlanTv.text = "مدت تمرین : ${model.getTime()} ساعت / شدت تمرین : %${model.getIntensity()} / استراحت بین هر ست : ${model.getRestTime()} ثانیه / مکث در حرکات : ${model.getPauseTime()} ثانیه"

    holder.itemView.setOnLongClickListener {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("حذف")
            .setMessage("آیا مطمئن هستید که می خواهید این مورد را حذف کنید؟")
            .setPositiveButton("تایید") { _, _ ->
                // حذف همه ویوها به جز اولین
                while (holder.tableLl.childCount > 1) {
                    holder.tableLl.removeViewAt(1)
                }
                planArrayList.removeAt(position)
                notifyDataSetChanged()
                Toast.makeText(context, "حذف شد", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("لغو") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
            .setCanceledOnTouchOutside(false)
        true
    }
}


}