package com.magang.projectmaganglatihan.adapter


import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.magang.projectmaganglatihan.R
import com.magang.projectmaganglatihan.model.MyProfileResponse
import kotlinx.android.synthetic.main.activity_profil.view.*
import kotlinx.android.synthetic.main.item_list.view.*

class MyProfileAdapter (private val context: Activity, private var listProfile: ArrayList<MyProfileResponse.Data>)
    : ArrayAdapter<String>(context, R.layout.activity_profil) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_profil, parent, false)

        val username = view.findViewById<TextView>(R.id.tvUsername)
        val jobdesk = view.findViewById<TextView>(R.id.tvJobDeskUser)
        val nip = view.findViewById<TextView>(R.id.tvNipUser)

        val item = listProfile[position]
        username.text = item.employeeFullname
        jobdesk.text = item.departement.departementTitle
        nip.text = item.employeeNik


        return view
    }

}