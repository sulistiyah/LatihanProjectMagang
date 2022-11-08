package com.magang.projectmaganglatihan.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.magang.projectmaganglatihan.R
import com.magang.projectmaganglatihan.databinding.ActivityLupaPassBinding

class LupaPassActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLupaPassBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLupaPassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        backPage()

    }

    private fun backPage() {
        binding.backTop.setOnClickListener {
            onBackPressed()
        }
    }

}