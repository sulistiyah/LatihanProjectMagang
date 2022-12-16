package com.magang.projectmaganglatihan.activity

import android.Manifest
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.magang.projectmaganglatihan.api.RetrofitClient
import com.magang.projectmaganglatihan.model.LoginResponse
import com.magang.projectmaganglatihan.R
import com.magang.projectmaganglatihan.model.LoginParam
import com.magang.projectmaganglatihan.storage.SharedPrefManager
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.etEmail
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private var mIsShowPass = false
    private lateinit var sharedPref: SharedPrefManager
    private lateinit var  locationRequest:LocationRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        sharedPref = SharedPrefManager(this)


        btnLogin()
        showLupaPass()
        showRegister()
        showHidePassword()
        checklocationPermission()

    }

    override fun onStart() {
        super.onStart()
        if (sharedPref.islogin) {
            val intentlogin = Intent(this@LoginActivity, HomeActivity::class.java)
            startActivity(intentlogin)
            sharedPref.tokenLogin
            finish()
        }
    }

    private fun showHidePassword() {
        //on off visibility password
        imgeye.setOnClickListener{
            mIsShowPass =! mIsShowPass
            showPassword(mIsShowPass)
        }
        showPassword(mIsShowPass)
    }

    private  fun showPassword(isShow: Boolean){
        if (isShow){
            etPass.transformationMethod = HideReturnsTransformationMethod.getInstance()
            imgeye.setImageResource(R.drawable.ic_hide_password)
        }else{
            etPass.transformationMethod = PasswordTransformationMethod.getInstance()
            imgeye.setImageResource(R.drawable.ic_show_password)
        }
        etPass.setSelection(etPass.text.toString().length)
    }


    private fun tokenSplash() {
        sharedPref = SharedPrefManager(this)
        sharedPref.tokenSplash
    }


    private fun btnLogin() {
        btnmasuk.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPass.text.toString().trim()

            if (email.isEmpty()) {
                etEmail.error = "dibutuhkan email"
                etEmail.requestFocus()
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                etPass.error = "dibutuhkan password"
                etPass.requestFocus()
                return@setOnClickListener
            }

            sharedPref = SharedPrefManager(this)


            RetrofitClient.instance.userLogin(LoginParam(
                username = "${etEmail.text}",
                password = "${etPass.text}"),
                "Bearer ${sharedPref.tokenSplash}")
                .enqueue(object : Callback<LoginResponse> {
                    override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                        if (response.isSuccessful) {
                            if (response.code() == 200) {

                                SharedPrefManager.getInstance(this@LoginActivity).savelogin(true)
                                SharedPrefManager.getInstance(this@LoginActivity).saveTokenLogin(response.body()?.data!!.token)
                                SharedPrefManager.getInstance(this@LoginActivity).saveEmployeeId(response.body()?.data!!.user.employeeId.toString())
                                SharedPrefManager.getInstance(this@LoginActivity).saveCompanyId(response.body()?.data!!.user.companyId.toString())
                                SharedPrefManager.getInstance(this@LoginActivity).saveEmployeeFullname(response.body()?.data!!.user.employeeFullname)

                                val intentlogin = Intent(this@LoginActivity, HomeActivity::class.java)
                                startActivity(intentlogin)

                                Toast.makeText(this@LoginActivity,"Berhasil...",Toast.LENGTH_SHORT).show()

                            } else {
                                Toast.makeText(this@LoginActivity,response.body()!!.statusCode, Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this@LoginActivity, "${response.body()?.message}",
                                Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()

                    }
                })

        }
    }


    private fun showRegister() {
        tvdaftar.setOnClickListener {
            intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            tokenSplash()
        }
    }

    private fun showLupaPass() {
        lupa_kata_s.setOnClickListener {
            intent = Intent(this, LupaPassActivity::class.java)
            startActivity(intent)
//            tokenSplash()
        }
    }
        private fun checklocationPermission(){
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED)
            {
                //when permission is already grant
                checkGPS()
            }else{
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    100
                )
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
            this.applicationContext)

            .checkLocationSettings(builder.build())

        result.addOnCompleteListener { task ->
            try {
                //saat GPS aktif
                 val result = task.getResult(ApiException ::class.java)


            }catch (e : ApiException){
                //saat GPS tidak aktif

                e.printStackTrace()
                when(e.statusCode){

                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try{
                        //tempat mengizinkan GPS
                        val resolveApiException = e as ResolvableApiException
                        resolveApiException.startResolutionForResult(this,200)

                    }catch (sendIntentException: IntentSender.SendIntentException){
                        }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE ->{

                    }
                }


            }
        }

    }


}



