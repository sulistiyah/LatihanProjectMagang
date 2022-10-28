package com.magang.projectmaganglatihan.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.magang.projectmaganglatihan.R
import com.magang.projectmaganglatihan.databinding.ActivityChangePasswordBinding

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangePasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        backPage()
        saveChangePassword()

    }

    private fun backPage() {
        binding.backTop.setOnClickListener {
            onBackPressed()
        }
    }

    private fun saveChangePassword() {
        binding.btnSimpan.setOnClickListener {

        }
    }

}