package com.magang.projectmaganglatihan.activity


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.magang.projectmaganglatihan.R
import com.magang.projectmaganglatihan.api.RetrofitClient
import com.magang.projectmaganglatihan.model.TokenParam
import com.magang.projectmaganglatihan.model.TokenResponse
import com.magang.projectmaganglatihan.storage.SharedPrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        RetrofitClient.instance.token(TokenParam(client = "app-android", secret = "TWTpknTux7PzuqDh6qLJQPXNvRT3an7B"))
            .enqueue(object : Callback<TokenResponse>{
                override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
                    if(response.isSuccessful){
                        if (response.code()==200){

                            SharedPrefManager.getInstance(this@SplashActivity).saveTokenSplash(response.body()!!.data.token)

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

}