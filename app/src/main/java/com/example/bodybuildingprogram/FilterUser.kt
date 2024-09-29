package com.example.bodybuildingprogram

import android.widget.Filter

class FilterUser: Filter {
    private val filterList: ArrayList<User>
    private var adapterUser: AdapterUser

    constructor(filterList: ArrayList<User>, adapterUser: AdapterUser){
        this.filterList = filterList
        this.adapterUser = adapterUser
    }


    override fun performFiltering(constraint: CharSequence?): FilterResults {
        var constraint = constraint
        val results = FilterResults()
        if (constraint != null && constraint.isNotEmpty()){
            //searched value is not null not empty

            //change to upper case, or lower case to avoid case sensitivity
            constraint = constraint.toString().uppercase()
            val filteredModels:ArrayList<User> = ArrayList()
            for (i in 0 until filterList.size){
                val fullName: String = "${filterList[i].getFirstName()} ${filterList[i].getLastName()}"
                //validate
                if (fullName.uppercase().contains(constraint)){
                    //add to filtered List
                    filteredModels.add(filterList[i])
                }
            }
            results.count = filteredModels.size
            results.values = filteredModels
        }
        else{
            //search value is either null or empty
            results.count = filterList.size
            results.values = filterList
        }
        return results  //don't miss it
    }

    override fun publishResults(constraint: CharSequence?, results: FilterResults) {
        //apply filter changes
        adapterUser.userArrayList = results.values as ArrayList<User>

        //notify changes
        adapterUser.notifyDataSetChanged()
    }
}