package com.magang.projectmaganglatihan.activity


import android.Manifest
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Geocoder
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.magang.projectmaganglatihan.R
import com.magang.projectmaganglatihan.databinding.ActivityHomeBinding
import com.magang.projectmaganglatihan.storage.SharedPrefManager
import java.io.IOException
import java.util.*

class HomeActivity : AppCompatActivity() {

    private lateinit var sharedPref: SharedPrefManager
//    private lateinit var tvusername: TextView
//    private lateinit var employeeFullname: String
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var tvlokasi: TextView
    private lateinit var binding : ActivityHomeBinding
    private lateinit var locationRequest: com.google.android.gms.location.LocationRequest


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = SharedPrefManager(this)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        tvlokasi = findViewById(R.id.tvLokasi)
        binding.tvLokasi.setOnClickListener(){
            checklocationPermission()
        }
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

    private fun checklocationPermission(){
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ){
            // saat perizinan nya sudah diberikan
            checkGPS()

        }else{
            //saat perizinanan ditolak
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),100)
        }
    }

    private fun checkGPS() {
        locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 5000
        locationRequest.fastestInterval = 2000

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        builder.setAlwaysShow(true)

        val result = LocationServices.getSettingsClient(
            this.applicationContext
        )
            .checkLocationSettings(builder.build())

        result.addOnCompleteListener{task ->
            try {
                //saat GPS nya aktif
                val response = task.getResult(
                    ApiException::class.java
                )
                getUserLocation()

            }catch (e: ApiException){
                //saat GPS nya tidak aktif
                e.printStackTrace()

                when(e.statusCode){
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try{
                        //tempat mengirim permintaan untuk GPS
                        val resolveApiException = e as ResolvableApiException
                        resolveApiException.startResolutionForResult(this,200)

                }catch (sendIntentException : IntentSender.SendIntentException){}
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        //saat setting tidak tersedia
                    }
                }
                }
            }
        }

    private fun getUserLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ){

        }
        fusedLocationProviderClient.lastLocation.addOnCompleteListener{task ->

            val location = task.getResult()

            if (location != null){
                try {
                    val geocoder = Geocoder(this, Locale.getDefault())

                    val address = geocoder.getFromLocation(location.latitude,location.longitude,1)

                    //mengatur address pada text view

                    val address_line = address!![0].getAddressLine(0)
                    binding.tvLokasi.setText(address_line)
                    val address_location = address!![0].getAddressLine(0)

                    openLocation(address_location.toString())

                }catch (e: IOException){

                }
            }
        }
    }

    private fun openLocation(location: String) {
        //membuka lokasi di google maps

        //atur button click
        binding.tvLokasi.setOnClickListener {
            if (!binding.tvLokasi.text.isEmpty()) {
                val uri = Uri.parse("geo:0, 0?q=$location")
                val intent = Intent(Intent.ACTION_VIEW, uri)
                intent.setPackage("com.google.android.app.maps")
                startActivity(intent)
            }
        }
    }


//    private fun showUsername() {
//        sharedpref = SharedPrefManager(this)
//        employeeFullname = sharedpref.employeeFullname
//        tvusername = findViewById(R.id.tvUsername)
//        tvusername.text = employeeFullname
//    }
}
