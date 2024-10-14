package com.example.bodybuildingprogram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.example.bodybuildingprogram.databinding.ActivityUserdashboardBinding

class UserdashboardActivity : AppCompatActivity() {

    private lateinit var binding:ActivityUserdashboardBinding

    private var userId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserdashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = intent.getIntExtra("userId",0)
        loadDataUser()

        binding.supplementsTv.setOnClickListener {
            val intent = Intent(this@UserdashboardActivity, SupplementsAndVitaminActivity::class.java)
            intent.putExtra("userId", userId)
            intent.putExtra("title", binding.supplementsTv.text)
            this.startActivity(intent)
        }
        binding.vitaminTv.setOnClickListener {
            val intent = Intent(this@UserdashboardActivity, SupplementsAndVitaminActivity::class.java)
            intent.putExtra("userId", userId)
            intent.putExtra("title", binding.vitaminTv.text)
            this.startActivity(intent)
        }
        binding.exerciseTv.setOnClickListener {
            val intent = Intent(this@UserdashboardActivity, PlanActivity::class.java)
            intent.putExtra("userId", userId)
            intent.putExtra("title", binding.exerciseTv.text)
            this.startActivity(intent)
        }
        binding.backBtn.setOnClickListener {
            onBackPressed()
        }

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
}