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

class MyProfileAdapter (private val context: Activity, private var listProfile: ArrayList<MyProfileResponse.Data>)
    : ArrayAdapter<String>(context, R.layout.activity_profil) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {

        val inflater = context.layoutInflater
        val view = inflater.inflate(R.layout.activity_profil, null, true)

        val username = view.findViewById<TextView>(R.id.tvUsername)
        val jobdesk = view.findViewById<TextView>(R.id.tvJobDeskUser)
        val nip = view.findViewById<TextView>(R.id.tvNipUser)

        username.text = listProfile[position].employeeFullname
        jobdesk.text = listProfile[position].departement.departementTitle
        nip.text = listProfile[position].employeeNik

        return view
    }

}