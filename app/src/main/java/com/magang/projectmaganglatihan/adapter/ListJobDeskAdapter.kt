package com.magang.projectmaganglatihan.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.magang.projectmaganglatihan.databinding.ItemListBinding
import com.magang.projectmaganglatihan.model.RegisterDepartementListResponse
import com.magang.projectmaganglatihan.storage.SharedPrefManager
import kotlinx.android.synthetic.main.item_list.view.*

class ListJobDeskAdapter(
    private var listJobDesk: ArrayList<RegisterDepartementListResponse.Data>,
    val listener: OnAdapterListener

): RecyclerView.Adapter<ListJobDeskAdapter.RegisterViewHolder>() {

    inner class RegisterViewHolder(private val binding: ItemListBinding) : RecyclerView.ViewHolder(binding.root) {
//        fun bind (get : RegisterDepartementListResponse.Data) {
//            binding.tvTitleJobDesk.text = get.departementTitle
//        }
//        val item: TextView = itemView.findViewById(R.id.tvTitleJobDesk)
//        fun bind(registerDepartementListResponse: RegisterDepartementListResponse.Data) {
//            with(itemView) {
//                val text = "${registerDepartementListResponse.departementTitle}"
//                tvTitleJobDesk.text = text
//            }
//        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegisterViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
//        return RegisterViewHolder(view)
        val binding = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RegisterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RegisterViewHolder, position: Int) {
        val item = listJobDesk[position]
        holder.itemView.tvTitleJobDesk.text = item.departementTitle
        holder.itemView.setOnClickListener{
            listener.onClik(item)
        }
//        val itemList = listJobDesk[position]
//        holder.itemView.tvTitleJobDesk.text = itemList.departementTitle
//        holder.bind(list[position])
//        holder.bind(listJobDesk.get(position))

    }

    override fun getItemCount() : Int {
        return listJobDesk.size
    }

    fun setData (data : ArrayList<RegisterDepartementListResponse.Data>) {
        listJobDesk = data
        notifyDataSetChanged()
    }

    interface OnAdapterListener{
        fun onClik (result: RegisterDepartementListResponse.Data)
    }




}
