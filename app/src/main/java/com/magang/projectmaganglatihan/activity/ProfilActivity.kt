package com.magang.projectmaganglatihan.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationRequest.create
import com.magang.projectmaganglatihan.R
import com.magang.projectmaganglatihan.api.RetrofitClient
import com.magang.projectmaganglatihan.databinding.ActivityProfilBinding
import com.magang.projectmaganglatihan.model.MyProfileResponse
import com.magang.projectmaganglatihan.storage.SharedPrefManager
import kotlinx.android.synthetic.main.activity_profil.*
import kotlinx.android.synthetic.main.fragment_job_desk_bottom_sheet.*
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.MultipartBody.Part.Companion.createFormData
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class ProfilActivity : AppCompatActivity() {

    private lateinit var binding : ActivityProfilBinding


    private lateinit var sharedPref: SharedPrefManager
    private lateinit var imageUri : Uri



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = SharedPrefManager(this)
        backPage()
        logout()
        editProfil()
        getDataProfil()
        updateAvatar()


    }


    private fun backPage() {
        binding.backTop.setOnClickListener {
            onBackPressed()
        }
    }


    private fun logout() {
        binding.tvLogout.setOnClickListener {
            sharedPref.clear()
            val intent = Intent(this, SplashActivity::class.java)
            startActivity(intent)
            finish()
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

                            SharedPrefManager.getInstance(this@ProfilActivity).saveEmployeeFullname(response.body()?.data!!.employeeFullname)

//                            myProfileAdapter = MyProfileAdapter(this@ProfilActivity, list)
//                            myProfileAdapter.notifyDataSetChanged()

                        } else {
                            Toast.makeText(this@ProfilActivity,response.body()!!.statusCode, Toast.LENGTH_SHORT).show()
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


    private fun updateAvatar() {
        binding.tvChangePhoto.setOnClickListener {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT

                startActivityForResult(intent, 100)
            } else {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 10)
            }
            


        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK) {
            imageUri = data?.data!!
            binding.imgProfil.setImageURI(imageUri)
        }
//        if (requestCode == 100 && resultCode == RESULT_OK) {
//
//            imageUri = data?.data!!
//            binding.viewFoto.setImageURI(imageUri)
//
//        }
    }


}