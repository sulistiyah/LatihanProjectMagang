package com.magang.projectmaganglatihan.activity


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.magang.projectmaganglatihan.R
import com.magang.projectmaganglatihan.api.RetrofitClient
import com.magang.projectmaganglatihan.model.LoginParam
import com.magang.projectmaganglatihan.model.LoginResponse
import com.magang.projectmaganglatihan.model.TokenParam
import com.magang.projectmaganglatihan.model.TokenResponse
import com.magang.projectmaganglatihan.storage.SharedPrefManager
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SplashActivity : AppCompatActivity() {

    private lateinit var sharedPref : SharedPrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        sharedPref = SharedPrefManager(this)
        checkLogin()

    }

    private fun checkLogin() {

        Handler().postDelayed({
            if (sharedPref.islogin) {
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            } else {
                RetrofitClient.instance.token(TokenParam(client = "app-android", secret = "TWTpknTux7PzuqDh6qLJQPXNvRT3an7B"))
                    .enqueue(object : Callback<TokenResponse>{
                        override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
                            if(response.isSuccessful){
                                if (response.code()==200){

                                    SharedPrefManager.getInstance(this@SplashActivity).saveTokenSplash(response.body()!!.data.token.toString())

                                    startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                                    finish()
                                }
                            }
                        }

                        override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                            Log.e("test senin", "onFailure: ${t.message}" )
                            Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()
                        }

                    })
            }
        }, 1500)
    }


}