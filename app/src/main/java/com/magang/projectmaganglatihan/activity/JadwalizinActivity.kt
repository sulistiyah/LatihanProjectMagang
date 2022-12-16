package com.magang.projectmaganglatihan.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import com.magang.projectmaganglatihan.R
import com.magang.projectmaganglatihan.databinding.ActivityJadwalizinBinding
import com.magang.projectmaganglatihan.storage.SharedPrefManager
import kotlinx.android.synthetic.main.activity_jadwalizin.*

class JadwalizinActivity : AppCompatActivity() {
    private lateinit var  binding : ActivityJadwalizinBinding
    private lateinit var sharedPrefManager: SharedPrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJadwalizinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        backPage()
        btnizin()
    }

    private fun backPage() {
        binding.backJadwalizin.setOnClickListener {
            onBackPressed()
        }
    }

    private fun btnizin(){
        btnizinsekarang.setOnClickListener{
            val intentizin = Intent(this@JadwalizinActivity, MembuatJadwalActivity::class.java)
            startActivity(intentizin)
        }
    }

}