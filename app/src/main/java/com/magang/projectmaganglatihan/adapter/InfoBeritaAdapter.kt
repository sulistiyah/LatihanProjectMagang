package com.magang.projectmaganglatihan.adapter

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.magang.projectmaganglatihan.R
import com.magang.projectmaganglatihan.model.InfoBeritaResponse
import kotlinx.android.synthetic.main.activity_profil.view.*
import kotlinx.android.synthetic.main.item_info_berita.view.*
import org.w3c.dom.Text

class InfoBeritaAdapter(
    private var listInfo: List<InfoBeritaResponse.Data>,
    val listener: OnAdapterListener)
    : RecyclerView.Adapter<InfoBeritaAdapter.InfoViewHolder>() {

//    var onItemClick : ((InfoBeritaResponse.Data) -> Unit)? = null
//    private var onSelectedListener : OnItemClickListener? = null
//    var listener : OnAdapterListener? = null

    inner class InfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title : TextView = itemView.findViewById(R.id.tvTitle)
        val subTitle : TextView = itemView.findViewById(R.id.tvSubTitle)
        val gambar : ImageView = itemView.findViewById(R.id.imgGambar)
        val cvDetail : CardView = itemView.findViewById(R.id.cvDetailInfo)

//        init {
//            cvDetail.setOnClickListener {
//                onSelectedListener?.onItemClick(it, layoutPosition)
//            }
//        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_info_berita, parent, false)
        return InfoViewHolder(view)
    }

    override fun onBindViewHolder(holder: InfoViewHolder, position: Int) {
        val item = listInfo[position]
        holder.title.text = item.title
        holder.subTitle.text = item.subTitle
//        holder.gambar.setImageResource(item.imageUrl)
        Glide.with(holder.gambar)
            .load(item.imageUrl)
            .placeholder(R.drawable.img_placeholder)
            .error(R.drawable.img_placeholder)
            .centerCrop()
            .into(holder.gambar.imgGambar)
        holder.cvDetail.setOnClickListener {
            listener.onClik(item)
        }
//        holder.itemView.setOnClickListener{
//            onItemClick?.invoke(item)
//        }

//        holder.itemView.cvDetailInfo.setOnClickListener {
//            onSelectedListener?.onItemClick(it, position)
//        }


    }

    override fun getItemCount() : Int {
        return listInfo.size
    }

    fun setData (data : List<InfoBeritaResponse.Data>) {
        listInfo = data
        notifyDataSetChanged()
    }

    fun getData(): List<InfoBeritaResponse.Data>{
        return listInfo
    }

//    fun setOnClickItemListener (onClickItemListener: OnItemClickListener) {
//        this.onSelectedListener = onClickItemListener
//    }
//
//
//    interface OnItemClickListener {
//        fun onItemClick(item: View, position: Int)
//    }

    interface OnAdapterListener{
        fun onClik( result: InfoBeritaResponse.Data)
    }


}

