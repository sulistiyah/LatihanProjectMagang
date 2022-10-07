package com.magang.projectmaganglatihan.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.magang.projectmaganglatihan.model.RegisterDepartementList
import com.magang.projectmaganglatihan.R
import kotlinx.android.synthetic.main.item_list.view.*

class ListJobDeskAdapter (var listJobDesk: ArrayList<RegisterDepartementList>):
    RecyclerView.Adapter<ListJobDeskAdapter.RegisterViewHolder>() {
    class RegisterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegisterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return RegisterViewHolder(view)
    }

    override fun onBindViewHolder(holder: RegisterViewHolder, position: Int) {
        val itemList = listJobDesk[position]
        holder.itemView.listJobDesk.text = itemList.data.toString()
        //        holder.itemListJob.text = itemList.toString()
//        val list = list[position]
//        holder.itemView.tv_email.text = list.email
//        holder.itemView.tv_name.text = list.first_name + " " + list.last_name
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
