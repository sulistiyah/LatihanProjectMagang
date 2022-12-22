package com.magang.projectmaganglatihan.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.magang.projectmaganglatihan.R
import com.magang.projectmaganglatihan.api.RetrofitClient
import com.magang.projectmaganglatihan.databinding.ActivityEditProfilBinding
import com.magang.projectmaganglatihan.fragment.JobDeskBottomSheetFragment
import com.magang.projectmaganglatihan.model.EditProfilResponse
import com.magang.projectmaganglatihan.storage.SharedPrefManager
import kotlinx.android.synthetic.main.activity_profil.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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
        setText()
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
            tvJobDesk.text = it.departementTitle
        }
    }


    private fun setText() {

        val nik : TextView = findViewById(R.id.tvIdKaryawan)
//        val nama : EditText = findViewById(R.id.etNama)
        val email : TextView = findViewById(R.id.tvEmail)
        val jobDesk : TextView = findViewById(R.id.tvJobDeskDepartement)
//        val telp : EditText = findViewById(R.id.etNomorTelepon)

        nik.text = intent.getStringExtra("nikKaryawan")
//        nama.text = intent.getStringExtra("nama")
        email.text = intent.getStringExtra("email")
        jobDesk.text = intent.getStringExtra("jobDesk")
//        telp.text = intent.getStringExtra("noTelepon")

    }


    private fun saveEditProfil() {
        binding.btnSimpan.setOnClickListener {
            val idKaryawan = binding.tvIdKaryawan.text.toString()
            val namaLengkap = binding.etNama.text.toString()
            val email = binding.tvEmail.text.toString()
            val jobDeskDepartement = binding.tvJobDeskDepartement.text.toString()
            val noTelp = binding.etNotelp.text.toString()



            val companyId : RequestBody = sharedPref.companyId.toRequestBody("text/plain".toMediaTypeOrNull())
            val employeeId: RequestBody = sharedPref.employeeId.toRequestBody("text/plain".toMediaTypeOrNull())
            val employeeFullName: RequestBody = namaLengkap.toRequestBody("text/plain".toMediaTypeOrNull())
            val departementId : RequestBody = jobDeskDepartement.toRequestBody("text/plain".toMediaTypeOrNull())
            val noTelepon : RequestBody = noTelp.toRequestBody("text/plain".toMediaTypeOrNull())

            RetrofitClient.instance.postEditProfil(
                "Bearer ${sharedPref.tokenLogin}",
                companyId,
                employeeId,
                employeeFullName,
                departementId,
                noTelepon).enqueue(object : Callback<EditProfilResponse> {
                override fun onResponse(
                    call: Call<EditProfilResponse>,
                    response: Response<EditProfilResponse>
                ) {
                    if (response.isSuccessful) {
                        if (response.code() == 200) {

                            Toast.makeText(this@EditProfilActivity, "Berhasil", Toast.LENGTH_SHORT).show()
                            onBackPressed()

                        } else {
                            Toast.makeText(this@EditProfilActivity,response.body()!!.statusCode, Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@EditProfilActivity, "${response.body()?.message}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<EditProfilResponse>, t: Throwable) {
                    Log.e("updateProfil", "onFailure: " + t.message )
                    Toast.makeText(this@EditProfilActivity, t.message, Toast.LENGTH_SHORT).show()
                }

            })



        }
    }


}