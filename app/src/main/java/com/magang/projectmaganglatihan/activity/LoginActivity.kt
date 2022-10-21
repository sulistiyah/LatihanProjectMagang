package com.magang.projectmaganglatihan.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Toast
import com.magang.projectmaganglatihan.api.RetrofitClient
import com.magang.projectmaganglatihan.model.LoginResponse
import com.magang.projectmaganglatihan.R
import com.magang.projectmaganglatihan.model.LoginParam
import com.magang.projectmaganglatihan.storage.SharedPrefManager
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private var mIsShowPass = false
    private lateinit var sharedPref: SharedPrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        etEmail.setText("aldos@gmail.com")
        etPass.setText("123123")


        sharedPref = SharedPrefManager(this)

        tvdaftar.setOnClickListener {
            intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            init()
        }

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
                "Bearer ${sharedPref.token}")
                .enqueue(object : Callback<LoginResponse> {
                    override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                        if (response.isSuccessful) {
                            if (response.code() == 200) {

                                SharedPrefManager.getInstance(applicationContext).savelogin(true)
                                SharedPrefManager.getInstance(applicationContext).saveToken(response.body()?.data!!.token)
                                SharedPrefManager.getInstance(applicationContext).saveEmployeeFullname(
                                    response.body()?.data!!.user.employeeFullname)

                                    val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                                    startActivity(intent)
                                    Toast.makeText(applicationContext,"berhasil",Toast.LENGTH_SHORT).show()

                            } else {
                                Toast.makeText(this@LoginActivity,response.errorBody()?.string(),
                                    Toast.LENGTH_SHORT).show()
                            }
                        } else {
                                 Toast.makeText(this@LoginActivity, response.errorBody()?.string(),
                                    Toast.LENGTH_SHORT).show()
                              }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()

                    }
                })

        }


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

    private fun init() {
    sharedPref = SharedPrefManager(this)
        sharedPref.token

    }
    }

//    override fun onStart() {
//        super.onStart()
//        if(SharedPrefManager.getInstance(this).islogin){
//            val intentlogin = Intent(this@LoginActivity,MainActivity::class.java)
//            startActivity(intentlogin)
//            finish()


