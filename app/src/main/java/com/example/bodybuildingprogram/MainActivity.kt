package com.example.bodybuildingprogram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.Observer
import androidx.room.InvalidationTracker
import androidx.room.Transaction
import com.example.bodybuildingprogram.databinding.ActivityMainBinding
import java.lang.Exception
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapterUser: AdapterUser
    private lateinit var databaseConnection: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseConnection = DatabaseConnection.getInstance(this)
        loadUsers()

        binding.searchEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //called as and when user type anything
                try {
                    adapterUser.filter.filter(s)
                }
                catch (e : Exception){

                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

        binding.addBtn.setOnClickListener {
            startActivity(Intent(this@MainActivity, AddUserActivity::class.java))
        }
    }

    private fun loadUsers() {
        val userViewModel = UserViewModel(DatabaseConnection.getInstance(this).userDao())
        userViewModel.getAllUsers().observe(this,Observer{userList->

            adapterUser = AdapterUser(this@MainActivity, userList as ArrayList<User>)
            binding.usersRv.adapter = adapterUser

        })
    }
}