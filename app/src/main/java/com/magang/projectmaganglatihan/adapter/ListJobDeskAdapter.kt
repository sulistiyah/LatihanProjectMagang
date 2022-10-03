package com.magang.projectmaganglatihan.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.magang.projectmaganglatihan.model.RegisterDepartementList
import com.magang.projectmaganglatihan.R

class ListJobDeskAdapter(private val listJobDesk: ArrayList<RegisterDepartementList>):
    RecyclerView.Adapter<ListJobDeskAdapter.RegisterViewHolder>() {

    class RegisterViewHolder( itemView : View): RecyclerView.ViewHolder(itemView){
        val itemListJob : TextView = itemView.findViewById(R.id.listJobDesk)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegisterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return RegisterViewHolder(view)
    }

    override fun onBindViewHolder(holder: RegisterViewHolder, position: Int) {
        val itemList = listJobDesk[position]
//        holder.itemListJob.text = itemList.toString()
        holder.itemListJob.text = itemList.data.toString()
    }

    override fun getItemCount(): Int {
        return listJobDesk.size
    }


    fun addListJobDesk(items: ArrayList<RegisterDepartementList>){
        listJobDesk.addAll(items)
        notifyDataSetChanged()
    }

    fun clear(){
        listJobDesk.clear()
        notifyDataSetChanged()
    }



}
