package com.magang.projectmaganglatihan.activity

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.magang.projectmaganglatihan.Api.RetrofitClient
import com.magang.projectmaganglatihan.Models.RegisterCompanyCheck
import com.magang.projectmaganglatihan.Models.RegisterDepartementList
import com.magang.projectmaganglatihan.Models.RegisterResponse
import com.magang.projectmaganglatihan.R
import com.magang.projectmaganglatihan.adapter.RegisterAdapter
import com.magang.projectmaganglatihan.databinding.ActivityRegisterBinding
import com.magang.projectmaganglatihan.storage.SharedPrefManager
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegisterBinding

    private lateinit var sharedPrefManager: SharedPrefManager
    private lateinit var layoutManager: LinearLayoutManager
    private var showPass = false
    private var showKonfirmasiPass = false
    private lateinit var registerAdapter: RegisterAdapter
    private lateinit var company_code : String
    private val listCompany = ArrayList<RegisterCompanyCheck>()
    private val listDepartement = ArrayList<RegisterDepartementList>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        layoutManager = LinearLayoutManager(this)

        binding.backTop.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btnDaftar.setOnClickListener {
            val kodePerusahaan = binding.etKodePerusahaan.text.toString()
            val idKaryawaan = binding.etIdKaryawan.text.toString()
            val namaLengkap = binding.etNamaLengkap.text.toString()
            val email = binding.etEmail.text.toString()
            val jobDeskDepartement = binding.etJobDeskDepartemen.text.toString()
            val nomorTelepon = binding.etNomorTelepon.text.toString()
            val password = binding.etMasukanPassword.text.toString()
            val konfirmasiPass = binding.etKonfirmasiPassword.text.toString()

            if (kodePerusahaan.isEmpty()) {
                etKodePerusahaan.error = "Tidak Boleh Kosog"
                etKodePerusahaan.requestFocus()
                return@setOnClickListener
            } else if (idKaryawaan.isEmpty()) {
                etIdKaryawan.error = "Tidak Boleh Kosog"
                etIdKaryawan.requestFocus()
                return@setOnClickListener
            } else if (email.isEmpty()) {
                etEmail.error = "Tidak Boleh Kosog"
                etEmail.requestFocus()
                return@setOnClickListener
            } else if (jobDeskDepartement.isEmpty()) {
                etJobDeskDepartemen.error = "Tidak Boleh Kosog"
                etJobDeskDepartemen.requestFocus()
                return@setOnClickListener
            } else if (nomorTelepon.isEmpty()) {
                etNomorTelepon.error = "Tidak Boleh Kosog"
                etNomorTelepon.requestFocus()
                return@setOnClickListener
            } else if (password.isEmpty()) {
                etMasukanPassword.error = "Tidak Boleh Kosog"
                etMasukanPassword.requestFocus()
                return@setOnClickListener
            } else if (konfirmasiPass.isEmpty()) {
                etKonfirmasiPassword.error = "Tidak Boleh Kosog"
                etKonfirmasiPassword.requestFocus()
                return@setOnClickListener
            }

            RetrofitClient.instance.postDaftar().enqueue(object  : Callback<RegisterResponse> {
                override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                    if (response.isSuccessful) {
                        if (response.code() == 200) {
                            getKodePerusahaan()
                            getJobDeskDepartemen()
                            if (password == (konfirmasiPass)) {
                                Toast.makeText(this@RegisterActivity, "Password Tidak Sama!!", Toast.LENGTH_SHORT).show()
                            }
                            val intentDaftar = Intent(applicationContext, LoginActivity::class.java)
                            startActivity(intentDaftar)
                        } else {
                            Toast.makeText(this@RegisterActivity,response.message(), Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@RegisterActivity, response.code().toString(), Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()

                }
            })

        }


        binding.imgShowPass.setOnClickListener {
            showPass = !showPass
            showPassword(showPass)
        }

        binding.imgShowKonfirmasiPass.setOnClickListener {
            showKonfirmasiPass = !showKonfirmasiPass
            showKonfirmasiPassword(showKonfirmasiPass)
        }


    }

    private fun getKodePerusahaan() {
        val parameters = HashMap<String, String>()
        parameters["codelabs"] = company_code.toString()
        RetrofitClient.instance.getKodePerusahaan(parameters).enqueue(object : Callback<ArrayList<RegisterCompanyCheck>> {
            override fun onResponse(call: Call<ArrayList<RegisterCompanyCheck>>, response: Response<ArrayList<RegisterCompanyCheck>>) {
                val listCompanyResponse = response.body()
                if (listCompanyResponse != null) {
                    registerAdapter.addListCompany(listCompanyResponse)
                    Toast.makeText(this@RegisterActivity, "Kode Perusahaan Valid", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@RegisterActivity, "Kode Perusahaan Tidak Valid", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<ArrayList<RegisterCompanyCheck>>, t: Throwable) {
                Toast.makeText(this@RegisterActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun getJobDeskDepartemen() {
        val paramater = HashMap<String, String>()
        paramater["company_id"] = "1"
        RetrofitClient.instance.getJobDeskDapartemen(paramater).enqueue(object : Callback<ArrayList<RegisterDepartementList>>{
            override fun onResponse(call: Call<ArrayList<RegisterDepartementList>>, response: Response<ArrayList<RegisterDepartementList>>) {
                val listDepartementResponse = response.body()
                if (listDepartementResponse != null) {
                    registerAdapter.addListDepartement(listDepartement)

                }
            }

            override fun onFailure(call: Call<ArrayList<RegisterDepartementList>>, t: Throwable) {

            }

        })

    }


    private fun showPassword(isShow: Boolean) {
        if (isShow) {
            etMasukanPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            imgShowPass.setImageResource(R.drawable.ic_hide_password)
        } else {
            etMasukanPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            imgShowPass.setImageResource(R.drawable.ic_show_password)
        }
        etMasukanPassword.setSelection(etMasukanPassword.text.toString().length)
    }

    private fun showKonfirmasiPassword(isShow: Boolean) {
        if (isShow) {
            etKonfirmasiPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            imgShowKonfirmasiPass.setImageResource(R.drawable.ic_hide_password)
        } else {
            etKonfirmasiPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            imgShowKonfirmasiPass.setImageResource(R.drawable.ic_show_password)
        }
        etKonfirmasiPassword.setSelection(etKonfirmasiPassword.text.toString().length)
    }


}

