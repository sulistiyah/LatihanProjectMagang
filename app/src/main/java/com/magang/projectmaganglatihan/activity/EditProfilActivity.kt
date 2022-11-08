package com.magang.projectmaganglatihan.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.magang.projectmaganglatihan.R
import com.magang.projectmaganglatihan.databinding.ActivityEditProfilBinding
import com.magang.projectmaganglatihan.databinding.ActivityRegisterBinding
import com.magang.projectmaganglatihan.fragment.JobDeskBottomSheetFragment
import com.magang.projectmaganglatihan.storage.SharedPrefManager
import retrofit2.http.Field

class EditProfilActivity : AppCompatActivity() {

    private lateinit var binding : ActivityEditProfilBinding


    private var companyId = 0
    private var bottomSheetDialogFragment = JobDeskBottomSheetFragment()
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var sharedPref: SharedPrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        backPage()
        changePassword()
        saveEditProfil()
        showBottomSheetDialogFragment()

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

    private fun showBottomSheetDialogFragment() {
        val tvJobDesk = findViewById<TextView>(R.id.tvJobDeskDepartement)
        tvJobDesk.setOnClickListener {
            bottomSheetDialogFragment.companyId = companyId
            bottomSheetDialogFragment.show(supportFragmentManager, "")

        }

        bottomSheetDialogFragment.listener = {
            tvJobDesk.setText("${it.departementTitle}")
        }
    }

    private fun saveEditProfil() {
        binding.btnSimpan.setOnClickListener {
            val idKaryawan = binding.etIdKaryawan.text.toString()
            val namaLengkap = binding.etNama.text.toString()
            val email = binding.etEmail.text.toString()
            val jobDeskDepartement = binding.tvJobDeskDepartement.text.toString()
            val noTelp = binding.etNotelp.text.toString()


        }
    }


}