package com.magang.projectmaganglatihan.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.magang.projectmaganglatihan.model.RegisterDepartementList
import com.magang.projectmaganglatihan.R

class ListJobDeskAdapter (private val listJobDesk: ArrayList<RegisterDepartementList>):
    RecyclerView.Adapter<ListJobDeskAdapter.RegisterViewHolder>() {

    inner class RegisterViewHolder( itemView : View): RecyclerView.ViewHolder(itemView){
        fun bindDepartement(registerDepartementList: RegisterDepartementList){

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegisterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return RegisterViewHolder(view)
    }

    override fun onBindViewHolder(holder: RegisterViewHolder, position: Int) {
        holder.bindDepartement(listJobDesk[position])
    }

    override fun getItemCount() = listJobDesk.size

    fun addListJobDesk(items: ArrayList<RegisterDepartementList>){
        listJobDesk.addAll(items)
        notifyDataSetChanged()
    }

    fun clear(){
        listJobDesk.clear()
        notifyDataSetChanged()
    }



}
