@file:Suppress("DEPRECATION")

package com.magang.projectmaganglatihan.activity


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.magang.projectmaganglatihan.R
import com.magang.projectmaganglatihan.storage.SharedPrefManager

class HomeActivity : AppCompatActivity() {

    private lateinit var sharedpref: SharedPrefManager
    private lateinit var tvusername: TextView
    private lateinit var employeeFullname: String
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var tvlokasi: TextView
    private lateinit var locationRequest: com.google.android.gms.location.LocationRequest


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        sharedpref = SharedPrefManager(this)
        showUsername()
        checkLogin()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        tvlokasi = findViewById(R.id.tvlokasi)
//        getCurrentLocation()

    }

    private fun showUsername() {
        sharedpref = SharedPrefManager(this)
        employeeFullname = sharedpref.employeeFullname
        tvusername = findViewById(R.id.tvusername)
        tvusername.text = employeeFullname
    }

    private fun checkLogin() {
        if (!sharedpref.islogin) {
            val intentCheckLogin = Intent(this, LoginActivity::class.java)
            startActivity(intentCheckLogin)
            finish()
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_ACCESS_LOCATION = 100

////    }
////    private  fun getCurrentLocation(){
////        if (checkPermission()){
////            if(isLocationEnabled()){
//
//            }else{
//                //Setting here
//            }
//        }else{
//            //Request permission here
//        }
//    }
    }
}
