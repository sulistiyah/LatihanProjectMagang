package com.magang.projectmaganglatihan.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.Toast
import com.magang.projectmaganglatihan.Api.RetrofitClient
import com.magang.projectmaganglatihan.Models.LoginParam
import com.magang.projectmaganglatihan.Models.LoginResponse
import com.magang.projectmaganglatihan.R
import com.magang.projectmaganglatihan.storage.SharedPrefManager
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private var mIsShowPass = false
    lateinit var sharedPref: SharedPrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        etEmail.setText("userdev@gmail.com")
        etPass.setText("123123")

        sharedPref = SharedPrefManager(this)

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
            RetrofitClient.instance.userLogin(LoginParam(email, password))
                .enqueue(object : Callback<LoginResponse> {
                    override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                        if (response.isSuccessful) {
                            if (response.code() == 200) {
                                Toast.makeText(
                                    this@LoginActivity, "to next page",
                                    Toast.LENGTH_SHORT
                                ).show()

                                SharedPrefManager.getInstance(applicationContext)
                                    .savelogin(true)
                                SharedPrefManager.getInstance(applicationContext)
                                    .saveToken(response.body()?.token.toString())
                                val intentlogin = Intent(this@LoginActivity, MainActivity::class.java)
                                startActivity(intentlogin)

                            } else {
                                Toast.makeText(
                                    this@LoginActivity, response.body()?.error,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Toast.makeText(
                                this@LoginActivity, response.code()?.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()

                    }
                })

        }
        //       txt_SignUp.setOnClickListener {
//            intent = Intent(applicationContext, SignUpActivity::class.java)
//            startActivity(intent)
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
                imgeye.setImageResource(R.drawable.ic_visibilityoff)
            }else{
                etPass.transformationMethod = PasswordTransformationMethod.getInstance()
                imgeye.setImageResource(R.drawable.eyefill)
            }
            etPass.setSelection(etPass.text.toString().length)
        }
    }

//    override fun onStart() {
//        super.onStart()
//        if(SharedPrefManager.getInstance(this).islogin){
//            val intentlogin = Intent(this@LoginActivity,MainActivity::class.java)
//            startActivity(intentlogin)
//            finish()


