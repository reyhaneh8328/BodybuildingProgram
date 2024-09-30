package com.example.bodybuildingprogram

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.bodybuildingprogram.databinding.ActivitySupplementsAndVitaminBinding
import com.example.bodybuildingprogram.databinding.DialogNamepdfBinding
import com.example.bodybuildingprogram.databinding.DialogTextplanAddBinding

class SupplementsAndVitaminActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySupplementsAndVitaminBinding
    private lateinit var textPlanArrayList: ArrayList<ModelTextPlan>
    private lateinit var adapterTextPlan: AdapterTextPlan
    private var userId = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySupplementsAndVitaminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = intent.getIntExtra("userId",0)
        textPlanArrayList = ArrayList()
        loadDataUser()
        loadTitle()
        loadDataTextPlan()

        binding.backBtn.setOnClickListener {
            onBackPressed()
        }
        binding.addBtn.setOnClickListener {
            addTextPlan()
        }
        binding.receivePdfBtn.setOnClickListener {
            generatePdf()
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
                val pdfGenerator = PdfGenerator(this)
                pdfGenerator.createPDFFromView(relativeLayout,namePdf)
            }
        }
    }

    private fun addTextPlan() {
        val textPlanAddBinding = DialogTextplanAddBinding.inflate(LayoutInflater.from(this))
        val builder = AlertDialog.Builder(this, R.style.CustomDialog)
        builder.setView(textPlanAddBinding.root)

        //create and show alert dialog
        val alertDialog = builder.create()
        alertDialog.show()
        alertDialog.setCanceledOnTouchOutside(false)

        textPlanAddBinding.backBtn.setOnClickListener { alertDialog.dismiss() }

        textPlanAddBinding.submitBtn.setOnClickListener {
            val nameText = textPlanAddBinding.nameEt.text.toString().trim()
            val descriptionText = textPlanAddBinding.descriptionEt.text.toString().trim()
            if (nameText.isEmpty()){
                Toast.makeText(this, "عنوان را وارد کنید ...", Toast.LENGTH_SHORT).show()
            }else if (descriptionText.isEmpty()){
                Toast.makeText(this, "توضیحات را وارد کنید ...", Toast.LENGTH_SHORT).show()
            }
            else{
                alertDialog.dismiss()
                val modelTextPlan = ModelTextPlan(nameText,descriptionText)
                textPlanArrayList.add(modelTextPlan)
            }
        }
    }

    private fun loadDataTextPlan() {
        adapterTextPlan = AdapterTextPlan(this, textPlanArrayList)
        binding.textRv.adapter = adapterTextPlan
    }

    private fun loadDataUser() {
        val userViewModel = UserViewModel(DatabaseConnection.getInstance(this).userDao())
        userViewModel.getUserById(userId).observe(this, Observer { user->
            binding.fullNameTv.text = "${user.getFirstName()} ${user.getLastName()}"
            binding.ageTv.text = "${user.getAge()}"
            binding.heightTv.text = "${user.getHeight()}"
            binding.weightTv.text = "${user.getWeight()}"
            binding.bloodTypeTv.text = user.getBloodType()
        })
    }
    private fun loadTitle() {
        val title = intent.getStringExtra("title")
        binding.titleTv.text = "$title روزانه باشگاه "
    }
}