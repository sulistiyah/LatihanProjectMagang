package com.magang.projectmaganglatihan.activity


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.*
import com.magang.projectmaganglatihan.R
import com.magang.projectmaganglatihan.adapter.InfoBeritaAdapter
import com.magang.projectmaganglatihan.adapter.MyProfileAdapter
import com.magang.projectmaganglatihan.api.RetrofitClient
import com.magang.projectmaganglatihan.databinding.ActivityHomeBinding
import com.magang.projectmaganglatihan.model.InfoBeritaResponse
import com.magang.projectmaganglatihan.model.MyProfileResponse
import com.magang.projectmaganglatihan.model.SetDataWajahResponse
import com.magang.projectmaganglatihan.storage.SharedPrefManager
import kotlinx.android.synthetic.main.activity_profil.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    private lateinit var sharedPref: SharedPrefManager
    private lateinit var myProfileAdapter: MyProfileAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private var list: ArrayList<MyProfileResponse.Data> = arrayListOf()
    private lateinit var tvUsername: TextView
    private lateinit var employeeFullname: String
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var imageUri : Uri
    private lateinit var file : File
    private lateinit var cameraIntent : Intent
    private val CAMERA_REQUEST_CODE = 1
//    private lateinit var locationRequest: com.google.android.gms.location.LocationRequest


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = SharedPrefManager(this)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        checkLogin()
        showUsername()
        getProfil()
        getListInfo()
        getUserLocation()
        setAbsen()
        checkAvatar()
        checkChangePass()



    }

    override fun onStart() {
        super.onStart()
//        if (!sharedPref.checkData) {
//            openDialog()
//            finish()
//        }

        openDialog()


//        val intent = Intent(this, DetectorActivity::class.java)
//        startActivity(intent)

    }

    private fun getProfil() {
        binding.imgProfil.setOnClickListener {
            intent = Intent(this, ProfilActivity::class.java)
            startActivity(intent)
            sharedPref.employeeId
            tokenLogin()
        }
    }

    private fun setAbsen() {
        binding.btnAbsenSekarang.setOnClickListener{
            val intent = Intent(this, DetectorActivity::class.java)
            startActivity(intent)
        }
    }

    private fun openDialog() {
        val openDialog = AlertDialog.Builder(this)
        openDialog.setTitle("Wajah anda belum terdaftar")
        openDialog.setMessage("Daftarkan data wajah anda.")
        openDialog.setPositiveButton("OK") { dialog,_->
//            setDataWajah()
            val intent = Intent(this@HomeActivity, DetectorActivity::class.java)
            startActivity(intent)
            dialog.dismiss()
        }
        openDialog.create()
        openDialog.show()
    }

    private fun setDataWajah() {

        val employeeId : RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), sharedPref.employeeId)

        RetrofitClient.instance.postSetDataWajah("Bearer ${sharedPref.tokenLogin}", employeeId)
            .enqueue(object : Callback<SetDataWajahResponse> {
                override fun onResponse(
                    call: Call<SetDataWajahResponse>,
                    response: Response<SetDataWajahResponse>
                ) {
                    if (response.isSuccessful) {
                        if (response.code() == 200) {

                            SharedPrefManager.getInstance(this@HomeActivity).saveCheckData(true)
//                            openCamera()
                            val intent = Intent(this@HomeActivity, DetectorActivity::class.java)
                            startActivity(intent)


                        } else {
                            Toast.makeText(this@HomeActivity,
                                response.body()!!.statusCode,
                                Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@HomeActivity,
                            "${response.body()?.message}",
                            Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<SetDataWajahResponse>, t: Throwable) {
                    Log.e("data", "onFailure: " + t.message)
                    Toast.makeText(this@HomeActivity, t.message, Toast.LENGTH_SHORT).show()
                }

            })

    }


    private fun tokenLogin() {
        sharedPref = SharedPrefManager(this)
        sharedPref.tokenLogin
    }


    private fun checkLogin() {
        if (!sharedPref.islogin) {
            val intentCheckLogin = Intent(this, LoginActivity::class.java)
            startActivity(intentCheckLogin)
            finish()
        }
    }

    private fun checkAvatar() {
//        if (!sharedPref.avatar) {
//            val intent = Intent(this, ProfilActivity::class.java)
//            startActivity(intent)
//            finish()
//        }
    }

    private fun checkChangePass() {
//        if (sharedPref.changePass) {
//            val intent = Intent(this, HomeActivity::class.java)
//            startActivity(intent)
//            finish()
//        }
    }


    private fun showUsername() {
        sharedPref = SharedPrefManager(this)
        employeeFullname = sharedPref.employeeFullname
        tvUsername = findViewById(R.id.tvUsername)
        tvUsername.text = employeeFullname
    }





    private fun getListInfo() {
        binding.cvInfromasi.setOnClickListener {
            intent = Intent(this, InfoBeritaActivity::class.java)
            startActivity(intent)
            sharedPref.companyId
            tokenLogin()
        }
    }

    private fun getUserLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) { return }

        fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
            val location = task.result

            if (location != null) {
                try {
                    val geocoder = Geocoder(this, Locale.getDefault())

                    val address = geocoder.getFromLocation(location.latitude,location.longitude,1)
                    val address_line = address!![0].getAddressLine(0)
                    binding.tvLokasi.text = address_line

                    val address_location = address[0].getAddressLine(0)

                    openLocation(address_location.toString())

                } catch (_: IOException) {

                }
            }
        }
    }

    private fun openLocation(location: String) {
        //membuka lokasi di google maps

        //atur button click
        binding.tvLokasi.setOnClickListener {
            if (binding.tvLokasi.text.isNotEmpty()) {
                val uri = Uri.parse("geo:0, 0?q=$location")
                val intent = Intent(Intent.ACTION_VIEW, uri)
                intent.setPackage("com.google.android.apps.maps")
                startActivity(intent)
            }
        }
    }

}
