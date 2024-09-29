package com.example.bodybuildingprogram

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.bodybuildingprogram.databinding.ActivityAddUserBinding

class AddUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddUserBinding
    private lateinit var databaseConnection: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseConnection = DatabaseConnection.getInstance(this)

        binding.backBtn.setOnClickListener {
            onBackPressed()
        }

        binding.bloodTypeTv.setOnClickListener {
            bloodTypePickDialog()
        }
        binding.submitBtn.setOnClickListener {
            /*  1)validate data
                2)upload user to database storage
                3)Get url of uploaded pdf
                4)upload pdf info to firebase db  */
            validateData()
        }
    }
    private fun validateData() {
        val firstName = binding.nameEt.text.toString().trim()
        val lastName = binding.lastNameEt.text.toString().trim()
        val age = binding.ageEt.text.toString().trim().toIntOrNull()
        val height = binding.heightEt.text.toString().trim().toIntOrNull()
        val weight = binding.weightEt.text.toString().trim().toIntOrNull()
        val typeBlood = binding.bloodTypeTv.text.toString().trim()
        if (firstName.isEmpty()){
            Toast.makeText(this, "نام ورزشکار را وارد کنید ...", Toast.LENGTH_SHORT).show()
        } else if (lastName.isEmpty()){
            Toast.makeText(this, "نام خانوادگی ورزشکار را وارد کنید ...", Toast.LENGTH_SHORT).show()
        }else if (age == null || age < 0){
            Toast.makeText(this, "سن ورزشکار را به درستی وارد کنید ...", Toast.LENGTH_SHORT).show()
        }else if (height == null || height < 0){
            Toast.makeText(this, "قد ورزشکار را به درستی وارد کنید ...", Toast.LENGTH_SHORT).show()
        }else if (weight == null || weight < 0){
            Toast.makeText(this, "وزن ورزشکار را به درستی وارد کنید ...", Toast.LENGTH_SHORT).show()
        } else if (typeBlood.isEmpty()){
            Toast.makeText(this, "گروه خونی ورزشکار را انتخاب کنید ...", Toast.LENGTH_SHORT).show()
        }else{
            val user: User = User(firstName,lastName,age,height,weight,typeBlood)
            createUser(user)
        }
    }
    private fun createUser(user: User) {
        try {
            val userViewModel = UserViewModel(databaseConnection.userDao())
            userViewModel.addUser(user)
            Toast.makeText(this, "ورزشکار جدید ایجاد شد", Toast.LENGTH_SHORT).show()
        }catch (e: Exception){
            Toast.makeText(this, "failled ${e.message}", Toast.LENGTH_SHORT).show()
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