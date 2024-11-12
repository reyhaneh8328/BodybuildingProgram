package com.example.bodybuildingprogram

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.bodybuildingprogram.databinding.ActivityPlanBinding
import com.example.bodybuildingprogram.databinding.DialogNamepdfBinding
import com.example.bodybuildingprogram.databinding.DialogPlanBinding
import com.example.bodybuildingprogram.databinding.DialogTableBinding

class PlanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlanBinding
    private lateinit var planArrayList: ArrayList<ModelPlan>
    private lateinit var adapterPlan: AdapterPlan
    private var userId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = intent.getIntExtra("userId",0)
        planArrayList = ArrayList()
        loadDataUser()
        loadDataPlan()

        binding.backBtn.setOnClickListener {
            onBackPressed()
        }
        binding.addBtn.setOnClickListener {
            if (planArrayList.size != 6){
                addPlan()
            }
        }
        binding.receivePdfBtn.setOnClickListener {
            generatePdf()
        }
        binding.deleteBtn.setOnClickListener {
            if (planArrayList.isEmpty()){
                Toast.makeText(this, "موردی برای حذف وجود ندارد!!", Toast.LENGTH_SHORT).show()
            }else{
                deleteFromePlanArrayList()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun deleteFromePlanArrayList() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("حذف")
            .setMessage("آیا مطمئن هستید که می خواهید آخرین مورد را حذف کنید؟")
            .setPositiveButton("تایید") { a, d ->
                planArrayList.removeAt(planArrayList.lastIndex)
                adapterPlan.notifyDataSetChanged()
                Toast.makeText(this, "حذف شد", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("لغو") { a, d ->
                a.dismiss()
            }
            .show()
            .setCanceledOnTouchOutside(false)
    }

    var tableList = ArrayList<ModelTable>()
    @SuppressLint("SetTextI18n")
    private fun addPlan() {
        tableList.clear()
        val planAddBinding = DialogPlanBinding.inflate(LayoutInflater.from(this))
        val builder = AlertDialog.Builder(this, R.style.CustomDialog)
        builder.setView(planAddBinding.root)
        //create and show alert dialog
        val alertDialog = builder.create()
        alertDialog.show()
        alertDialog.setCanceledOnTouchOutside(false)

        planAddBinding.sizeTableTv.text = "تعداد تمرین = ${tableList.size}"

        planAddBinding.backBtn.setOnClickListener { alertDialog.dismiss() }
        planAddBinding.addBtn.setOnClickListener {
            addToTableList(planAddBinding.sizeTableTv)
        }
        planAddBinding.deleteBtn.setOnClickListener {
            if (tableList.isEmpty()){
                Toast.makeText(this, "موردی برای حذف وجود ندارد!!", Toast.LENGTH_SHORT).show()
            }else{
                deleteFromTableList()
            }
        }
        planAddBinding.submitBtn.setOnClickListener {
            val typePlanEt = planAddBinding.typePlanEt.text.toString().trim()
            val prePlanEt = planAddBinding.prePlanEt.text.toString().trim()
            val timeEt = planAddBinding.timeEt.text.toString().trim().toIntOrNull()
            val intensityEt = planAddBinding.intensityEt.text.toString().trim().toIntOrNull()
            val restTimeEt = planAddBinding.restTimeEt.text.toString().trim().toIntOrNull()
            val pauseTimeEt = planAddBinding.pauseTimeEt.text.toString().trim().toIntOrNull()
            if (typePlanEt.isEmpty()){
                Toast.makeText(this, "ناحیه اعمال فشار جلسه را وارد کنید ...", Toast.LENGTH_SHORT).show()
            }else if (prePlanEt.isEmpty()){
                Toast.makeText(this, "پیش تمرین را وارد کنید ...", Toast.LENGTH_SHORT).show()
            }else if (tableList.isEmpty()){
                Toast.makeText(this, "تمرین ها را اضافه کنید ...", Toast.LENGTH_SHORT).show()
            }else if (timeEt == null || timeEt <= 0){
                Toast.makeText(this, "زمان تمرین را وارد کنید ...", Toast.LENGTH_SHORT).show()
            }else if (intensityEt == null || intensityEt <= 0){
                Toast.makeText(this, "شدت تمرین را وارد کنید ...", Toast.LENGTH_SHORT).show()
            }else if (restTimeEt == null || restTimeEt <= 0){
                Toast.makeText(this, "زمان استراحت را وارد کنید ...", Toast.LENGTH_SHORT).show()
            }else if (pauseTimeEt == null || pauseTimeEt <= 0){
                Toast.makeText(this, "زمان مکث حرکات را وارد کنید ...", Toast.LENGTH_SHORT).show()
            }else{
                alertDialog.dismiss()
                submitAddPlan(typePlanEt,prePlanEt,timeEt,intensityEt,restTimeEt,pauseTimeEt)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun submitAddPlan(
        typePlanEt: String,
        prePlanEt: String,
        timeEt: Int,
        intensityEt: Int,
        restTimeEt: Int,
        pauseTimeEt: Int
    ) {
        val tableRowList = ArrayList<TableRow>()
        val density = resources.displayMetrics.density
        val sizePadding = (5 * density).toInt()
        var type = 0
        var background = 0
        for (i in tableList.indices){
            background = R.drawable.shape_table03
            if (type == 1){
                background = R.drawable.shape_table02
            }
            val tableRow = TableRow(this)
            val textViews = arrayListOf<TextView>(TextView(this), TextView(this), TextView(this),TextView(this))
            textViews[0].text = tableList[i].getNamePlan()
            textViews[0].layoutParams = TableRow.LayoutParams((0 * density).toInt(), TableRow.LayoutParams.MATCH_PARENT, 3f)
            textViews[1].text = buildString { append(tableList[i].getSetNumber()) }
            textViews[1].layoutParams = TableRow.LayoutParams((0 * density).toInt(), TableRow.LayoutParams.MATCH_PARENT, 1f)
            textViews[2].text = tableList[i].getSizeSet()
            textViews[2].layoutParams = TableRow.LayoutParams((0 * density).toInt(), TableRow.LayoutParams.MATCH_PARENT, 1f)
            textViews[3].text = tableList[i].getDescription()
            textViews[3].layoutParams = TableRow.LayoutParams((0 * density).toInt(), TableRow.LayoutParams.MATCH_PARENT, 2f)
            for (i in textViews.indices){
                textViews[i].setBackgroundResource(background)
                textViews[i].setTextSize(TypedValue.COMPLEX_UNIT_SP, 13F)
                textViews[i].textAlignment = View.TEXT_ALIGNMENT_CENTER
                textViews[i].setPadding(sizePadding,sizePadding,sizePadding,sizePadding)
                textViews[i].setTextColor(Color.BLACK)
                tableRow.addView(textViews[i])
            }
            tableRowList.add(tableRow)
            type = 1 - type

        }
        val typePlanEt = "جلسه ${SessionNumber.entries[planArrayList.size]} / $typePlanEt"
        val model = ModelPlan(typePlanEt,prePlanEt,tableRowList,timeEt,intensityEt,restTimeEt,pauseTimeEt)
        planArrayList.add(model)
        adapterPlan.notifyDataSetChanged()
        Toast.makeText(this, "اجرا شده****", Toast.LENGTH_SHORT).show()
    }

    private fun deleteFromTableList() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("حذف")
            .setMessage("آیا مطمئن هستید که می خواهید آخرین مورد را حذف کنید؟")
            .setPositiveButton("تایید") { a, d ->
                tableList.removeAt(tableList.lastIndex)
                Toast.makeText(this, "حذف شد", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("لغو") { a, d ->
                a.dismiss()
            }
            .show()
            .setCanceledOnTouchOutside(false)
    }

    @SuppressLint("SetTextI18n")
    private fun addToTableList(sizeTableTv: TextView) {
        val tableAddBinding = DialogTableBinding.inflate(LayoutInflater.from(this))
        val builder = AlertDialog.Builder(this, R.style.CustomDialog)
        builder.setView(tableAddBinding.root)
        //create and show alert dialog
        val alertDialog = builder.create()
        alertDialog.show()
        alertDialog.setCanceledOnTouchOutside(false)

        tableAddBinding.backBtn.setOnClickListener { alertDialog.dismiss() }

        tableAddBinding.submitBtn.setOnClickListener {
            val namePlan = tableAddBinding.namePlanEt.text.toString().trim()
            val setNumber = tableAddBinding.setNumberEt.text.toString().trim()
            val sizeSet = tableAddBinding.sizeSetEt.text.toString().trim()
            val description = tableAddBinding.descriptionEt.text.toString().trim()
            if (namePlan.isEmpty()){
                Toast.makeText(this, "نام تمرین را وارد کنید ...", Toast.LENGTH_SHORT).show()
            }else if (setNumber.isEmpty()){
                Toast.makeText(this, "تعداد ست را وارد کنید ...", Toast.LENGTH_SHORT).show()
            }else if (sizeSet.isEmpty()){
                Toast.makeText(this, "اندازه هر ست را وارد کنید ...", Toast.LENGTH_SHORT).show()
            }else{
                alertDialog.dismiss()
                val modelTable = ModelTable(namePlan,setNumber,sizeSet,description)
                tableList.add(modelTable)
                sizeTableTv.text = "تعداد تمرین = ${tableList.size}"
            }
        }
    }


    private fun generatePdf() {
        val getNamePdf = DialogNamepdfBinding.inflate(LayoutInflater.from(this))
        val builder = AlertDialog.Builder(this, R.style.CustomDialog)
        builder.setView(getNamePdf.root)

        val alertDialog = builder.create()
        alertDialog.show()
        alertDialog.setCanceledOnTouchOutside(false)

        getNamePdf.backBtn.setOnClickListener { alertDialog.dismiss() }

        getNamePdf.submitBtn.setOnClickListener {
            val namePdf = getNamePdf.nameEt.text.toString().trim()
            if (namePdf.isEmpty()) {
                Toast.makeText(this, "نام pdf را وارد کنید ...", Toast.LENGTH_SHORT).show()
            }else{
                alertDialog.dismiss()
                val relativeLayout = binding.pdfRl
                val recyclerView = binding.planRv
                val pdfGenerator = PdfGenerator(this)
//                pdfGenerator.createPDFFromView(relativeLayout,namePdf)
                pdfGenerator.createMultiPagePdfWithoutGap(relativeLayout,recyclerView,namePdf,binding.profileRl)
            }
        }
    }

    private fun loadDataPlan() {
        adapterPlan = AdapterPlan(this, planArrayList)
        binding.planRv.adapter = adapterPlan
    }

    @SuppressLint("SetTextI18n")
    private fun loadDataUser() {
        val userViewModel = UserViewModel(DatabaseConnection.getInstance(this).userDao())
        userViewModel.getUserById(userId).observe(this, Observer { user->
            binding.fullNameTv.text = "${user.getFirstName()} ${user.getLastName()}"
            binding.desiredTv.text = user.getDesired()
            binding.ageTv.text = "${user.getAge()}"
            binding.heightTv.text = "${user.getHeight()}"
            binding.weightTv.text = "${user.getWeight()}"
            binding.bloodTypeTv.text = user.getBloodType()
        })
    }
}