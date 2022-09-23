package com.magang.projectmaganglatihan.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.magang.projectmaganglatihan.R
import com.magang.projectmaganglatihan.storage.SharedPrefManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
//
//    private lateinit var  sharedPref : SharedPrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        sharedPref = SharedPrefManager(this)
//
//        btnlogout.setOnClickListener{
//            sharedPref.clear()
//            showMessage("keluar")
//            moveIntent()
//        }
//    }
//    private fun showMessage(message : String){
//        Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
//    }
//    private  fun moveIntent(){
//        startActivity(Intent (this,SplashActivity::class.java))
//        finish()
    }
}