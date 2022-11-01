package com.magang.projectmaganglatihan.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.magang.projectmaganglatihan.R
import com.magang.projectmaganglatihan.adapter.MyProfileAdapter
import com.magang.projectmaganglatihan.api.RetrofitClient
import com.magang.projectmaganglatihan.databinding.ActivityProfilBinding
import com.magang.projectmaganglatihan.model.MyProfileResponse
import com.magang.projectmaganglatihan.model.RegisterDepartementListResponse
import com.magang.projectmaganglatihan.storage.SharedPrefManager
import kotlinx.android.synthetic.main.activity_profil.*
import kotlinx.android.synthetic.main.fragment_job_desk_bottom_sheet.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfilActivity : AppCompatActivity() {

    private lateinit var binding : ActivityProfilBinding



    private lateinit var sharedPref: SharedPrefManager
    private lateinit var myProfileAdapter: MyProfileAdapter
    private lateinit var tokenLogin : String
    private var list : ArrayList<MyProfileResponse.Data> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = SharedPrefManager(this)
        backPage()
        logout()
        editProfil()
        getDataProfil()


    }


    private fun backPage() {
        binding.backTop.setOnClickListener {
            onBackPressed()
        }
    }


    private fun logout() {
        binding.tvLogout.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }


    private fun editProfil() {
        val editProfil = findViewById<ImageView>(R.id.imgEditProfil)
        editProfil.setOnClickListener{
            val intent = Intent(this, EditProfilActivity::class.java)
            startActivity(intent)
        }
    }


    private fun getDataProfil() {

        val parameter = HashMap<String, String>()
        parameter["employee_id"] = "${sharedPref.employeeId}"

        RetrofitClient.instance.getMyProfile(parameter, "Bearer ${sharedPref.tokenLogin}")
            .enqueue(object : Callback<MyProfileResponse> {
                override fun onResponse(call: Call<MyProfileResponse>, response: Response<MyProfileResponse>) {
                    if (response.isSuccessful) {
                        if (response.code() == 200) {

                            var username : String? = response.body()?.data!!.employeeFullname
                            var jobDesk : String? = response.body()?.data!!.departement.departementTitle
                            var nip : String? = response.body()?.data!!.employeeNik

                            tvUsername.setText(username)
                            tvJobDeskUser.setText(jobDesk)
                            tvNipUser.setText(nip)

//                            myProfileAdapter = MyProfileAdapter(this@ProfilActivity, list)
//                            myProfileAdapter.notifyDataSetChanged()

                        } else {
                            Toast.makeText(this@ProfilActivity,response.code(), Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@ProfilActivity, "${response.body()?.message}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<MyProfileResponse>, t: Throwable) {
                    Log.e("profil", "onFailure: " + t.message )
                    Toast.makeText(this@ProfilActivity, t.message, Toast.LENGTH_SHORT).show()
                }

            })

    }


}