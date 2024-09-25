package com.example.bodybuildingprogram

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.bodybuildingprogram.databinding.ActivityAddUserBinding

class AddUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddUserBinding
    private lateinit var databaseConnection: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseConnection = DatabaseConnection.getInstance()
        binding.backBtn.setOnClickListener {
            onBackPressed()
        }

        binding.bloodTypeTv.setOnClickListener {
            bloodTypePickDialog()
        }
    }
    private var selectedBloodType = ""
    private fun bloodTypePickDialog() {
        val bloodType = BloodType.values()
        var bloodTypeArray = arrayOfNulls<String>((bloodType.size)*2)
        for (i in 0..(bloodType.size)-1){
            bloodTypeArray[i] = "${bloodType[i]}-"
            bloodTypeArray[i+4] = "${bloodType[i]}+"
        }
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pick Category")
            .setItems(bloodTypeArray){dialog,which->
                selectedBloodType = bloodTypeArray[which].toString()
                binding.bloodTypeTv.text = selectedBloodType
            }
            .show()
    }
}