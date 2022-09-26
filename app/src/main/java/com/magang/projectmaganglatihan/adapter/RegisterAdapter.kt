package com.magang.projectmaganglatihan.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.magang.projectmaganglatihan.Models.RegisterCompanyCheck
import com.magang.projectmaganglatihan.Models.RegisterDepartementList
import com.magang.projectmaganglatihan.Models.RegisterResponse
import com.magang.projectmaganglatihan.R

class RegisterAdapter (val context: Context ): RecyclerView.Adapter<RegisterAdapter.RegisterViewHolder>() {

    private val listRegister : ArrayList<RegisterResponse> = arrayListOf()
    private val listCompany : ArrayList<RegisterCompanyCheck> = arrayListOf()
    private val listDepartement : ArrayList<RegisterDepartementList> = arrayListOf()

    inner class RegisterViewHolder( itemView : View): RecyclerView.ViewHolder(itemView){
        fun bind(registerResponse: RegisterResponse){

        }
        fun bindCompany(registerCompanyCheck: RegisterCompanyCheck){

        }
        fun bindDepartement(registerDepartementList: RegisterDepartementList){

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegisterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_register, parent, false)
        return RegisterViewHolder(view)
    }

    override fun onBindViewHolder(holder: RegisterViewHolder, position: Int) {
        holder.bind(listRegister[position])
        holder.bindCompany(listCompany[position])
        holder.bindDepartement(listDepartement[position])
    }

    override fun getItemCount(): Int {
        return listRegister.size
        return listCompany.size
        return listDepartement.size
    }


    fun addListCompany(itemCompany: ArrayList<RegisterCompanyCheck>){
        listCompany.addAll(itemCompany)
        notifyDataSetChanged()
    }

    fun addListDepartement(itemDepartement: ArrayList<RegisterDepartementList>){
        listDepartement.addAll(itemDepartement)
        notifyDataSetChanged()
    }



}
