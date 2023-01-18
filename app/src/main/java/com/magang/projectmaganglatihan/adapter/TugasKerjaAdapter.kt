package com.magang.projectmaganglatihan.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.magang.projectmaganglatihan.R
import com.magang.projectmaganglatihan.model.ListTugasKerjaResponse
import com.magang.projectmaganglatihan.model.TugasKerjaResponse
import kotlinx.android.synthetic.main.cardviewizin.view.*

class TugasKerjaAdapter (
    private var listTugas: List<TugasKerjaResponse.Data>,
    val listener: OnAdapterListener)
    : RecyclerView.Adapter<TugasKerjaAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val namaTugas : TextView = itemView.findViewById(R.id.tvNamaTugas)
        val tanggal : TextView = itemView.findViewById(R.id.tvHariTanggalJam)
        val reportStatus : TextView = itemView.findViewById(R.id.tvReportStatus)
        val bgReportStatus : LinearLayout = itemView.findViewById(R.id.bgReportStatus)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tugaskerja, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listTugas[position]
        holder.namaTugas.text = item.sprintLog.companyProject.companyProjectName
        holder.tanggal.text = item.workReportTime
        holder.reportStatus.text = item.reportStatus.reportStatusName
        holder.bgReportStatus.setOnClickListener {
            listener.onClik(item)
        }
    }

    override fun getItemCount() : Int {
        return listTugas.size
    }

    fun setData (data : List<TugasKerjaResponse.Data>) {
        listTugas = data
        notifyDataSetChanged()
    }

    fun getData(): List<TugasKerjaResponse.Data>{
        return listTugas
    }

    interface OnAdapterListener{
        fun onClik( result: TugasKerjaResponse.Data)
    }


}
