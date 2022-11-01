@file:Suppress("DEPRECATION")

package com.magang.projectmaganglatihan.activity


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.magang.projectmaganglatihan.R
import com.magang.projectmaganglatihan.storage.SharedPrefManager

class HomeActivity : AppCompatActivity() {

    private lateinit var sharedPref: SharedPrefManager
//    private lateinit var tvusername: TextView
//    private lateinit var employeeFullname: String
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var tvlokasi: TextView
    private lateinit var locationRequest: com.google.android.gms.location.LocationRequest


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        sharedPref = SharedPrefManager(this)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        tvlokasi = findViewById(R.id.tvLokasi)
//        showUsername()
        checkLogin()
        getProfil()
//        getCurrentLocation()

    }

    private fun checkLogin() {
        if (!sharedPref.islogin) {
            val intentCheckLogin = Intent(this, LoginActivity::class.java)
            startActivity(intentCheckLogin)
            finish()
        }
    }

    private fun getProfil() {
        val profil = findViewById<ImageView>(R.id.imgProfil)
        profil.setOnClickListener{
            intent = Intent(this, ProfilActivity::class.java)
            startActivity(intent)
            sharedPref.employeeId
            tokenLogin()
        }
    }

    private fun tokenLogin() {
        sharedPref = SharedPrefManager(this)
        sharedPref.tokenLogin
    }

//    private fun showUsername() {
//        sharedpref = SharedPrefManager(this)
//        employeeFullname = sharedpref.employeeFullname
//        tvusername = findViewById(R.id.tvUsername)
//        tvusername.text = employeeFullname
//    }




}
