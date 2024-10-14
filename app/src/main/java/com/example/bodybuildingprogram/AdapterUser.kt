package com.example.bodybuildingprogram

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.bodybuildingprogram.databinding.RowUserBinding

class AdapterUser: RecyclerView.Adapter<AdapterUser.HolderUser>, Filterable {
    private val context: Context
    public var userArrayList: ArrayList<User>
    private var filterList: ArrayList<User>

    private var filter: FilterUser? = null
    private lateinit var binding: RowUserBinding

    constructor(context: Context, userArrayList: ArrayList<User>) {
        this.context = context
        this.userArrayList = userArrayList
        this.filterList = userArrayList
    }
    inner class HolderUser(itemView: View): RecyclerView.ViewHolder(itemView){
        //init ui views
        var fullNameTv: TextView = binding.fullNameTv
        var deleteBtn: ImageButton = binding.deleteBtn
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderUser {
        binding = RowUserBinding.inflate(LayoutInflater.from(context), parent, false)
        return HolderUser(binding.root)
    }

    override fun getItemCount(): Int {
        return userArrayList.size
    }

    override fun onBindViewHolder(holder: HolderUser, position: Int) {
        /*------- Get Data, Set Data, Handle clicks etc -------*/

        //get data
        val model = userArrayList[position]
        val fullName = "${model.getFirstName()} ${model.getLastName()}"

        //set data
        holder.fullNameTv.text = fullName

        //handle click, delete User
        holder.deleteBtn.setOnClickListener {
            //confirm before delete
            val builder = AlertDialog.Builder(context)
            builder.setTitle("حذف")
                .setMessage("آیا مطمئن هستید که می خواهید این کاربر را حذف کنید؟")
                .setPositiveButton("تایید"){a, d->
                    Toast.makeText(context, "در حال حذف", Toast.LENGTH_SHORT).show()
                    deleteUser(model)
                }
                .setNegativeButton("لغو"){a, d->
                    a.dismiss()
                }
                .show()
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(context, UserdashboardActivity::class.java)
            intent.putExtra("userId", model.getUserId())
            context.startActivity(intent)

        }
    }

    private fun deleteUser(model: User) {
        val userViewModel: UserViewModel = UserViewModel(DatabaseConnection.getInstance(context).userDao())
        try {
            userViewModel.removeUser(model)
            Toast.makeText(context, "حذف شد...", Toast.LENGTH_SHORT).show()
        }
        catch (e:Exception){
            Toast.makeText(context, " حذف نشد به این دلیل ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getFilter(): Filter {
        if (filter == null){
            filter = FilterUser(filterList, this)
        }
        return filter as FilterUser
    }
}