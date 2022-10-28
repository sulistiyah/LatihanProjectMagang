package com.magang.projectmaganglatihan.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.magang.projectmaganglatihan.R
import com.magang.projectmaganglatihan.databinding.ActivityEditProfilBinding
import com.magang.projectmaganglatihan.databinding.ActivityRegisterBinding

class EditProfilActivity : AppCompatActivity() {

    private lateinit var binding : ActivityEditProfilBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        backPage()
        changePassword()
        saveEditProfil()

    }

    private fun backPage() {
        binding.backTop.setOnClickListener {
            onBackPressed()
        }
    }

    private fun changePassword() {
        binding.btnGantiPassword.setOnClickListener {
            val intent = Intent(this, ChangePasswordActivity::class.java )
            startActivity(intent)
        }
    }

    private fun saveEditProfil() {
        binding.btnSimpan.setOnClickListener {

        }
    }


}