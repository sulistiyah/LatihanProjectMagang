package com.magang.projectmaganglatihan.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.magang.projectmaganglatihan.R
import com.magang.projectmaganglatihan.model.ListTugasKerjaResponse

class ListTugasKerjaAdapter (
    private var listTugas: List<ListTugasKerjaResponse.Data>,
    val listener: OnAdapterListener)
: RecyclerView.Adapter<ListTugasKerjaAdapter.ViewHolder>() {

//    var onItemClick : ((InfoBeritaResponse.Data) -> Unit)? = null
//    private var onSelectedListener : OnItemClickListener? = null
//    var listener : OnAdapterListener? = null

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTugas : TextView = itemView.findViewById(R.id.tvTitleTugasKerja)
        val statusTugas : TextView = itemView.findViewById(R.id.tvStatusTugas)
        val cvDetailTugas : CardView = itemView.findViewById(R.id.cvDetailTugas)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_tugaskerja, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listTugas[position]
        holder.titleTugas.text = item.companyProjectName
        holder.statusTugas.text = item.statusText
        holder.cvDetailTugas.setOnClickListener {
            listener.onClik(item)
        }
    }

    override fun getItemCount() : Int {
        return listTugas.size
    }

    fun setData (data : List<ListTugasKerjaResponse.Data>) {
        listTugas = data
        notifyDataSetChanged()
    }

    fun getData(): List<ListTugasKerjaResponse.Data>{
        return listTugas
    }

    interface OnAdapterListener{
        fun onClik( result: ListTugasKerjaResponse.Data)
    }


}
