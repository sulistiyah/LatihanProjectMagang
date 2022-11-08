package com.magang.projectmaganglatihan.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.magang.projectmaganglatihan.R
import com.magang.projectmaganglatihan.api.RetrofitClient
import com.magang.projectmaganglatihan.databinding.ActivityRegisterBinding
import com.magang.projectmaganglatihan.fragment.JobDeskBottomSheetFragment
import com.magang.projectmaganglatihan.model.*
import com.magang.projectmaganglatihan.storage.SharedPrefManager
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.etEmail
import kotlinx.android.synthetic.main.activity_register.etIdKaryawan
import kotlinx.android.synthetic.main.activity_register.etKodePerusahaan
import kotlinx.android.synthetic.main.activity_register.etKonfirmasiPassword
import kotlinx.android.synthetic.main.activity_register.etMasukanPassword
import kotlinx.android.synthetic.main.activity_register.etNamaLengkap
import kotlinx.android.synthetic.main.activity_register.etNomorTelepon
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegisterBinding

//    private lateinit var selectItem: TextView
//    private lateinit var dialog: BottomSheetDialog
//    private lateinit var recyclerView: RecyclerView
//    private lateinit var listJobDeskAdapter : ListJobDeskAdapter
//    private var list : ArrayList<RegisterDepartementListResponse.Data> = arrayListOf()
//    private var companyCode = 0
    private var companyId = 0
    private var departementId = 0
    private var bottomSheetDialogFragment = JobDeskBottomSheetFragment()
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var sharedPref: SharedPrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        layoutManager = LinearLayoutManager(this)
        sharedPref = SharedPrefManager(this)

        backPage()
        kodePerusahaanFocusListener()
        showBottomSheetDialogFragment()
        matchPasswordListener()
        postDaftar()

    }


    private fun backPage() {
        binding.backTop.setOnClickListener {
            onBackPressed()
        }
    }


    private fun kodePerusahaanFocusListener() {
        binding.etKodePerusahaan.addTextChangedListener(object :TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0 != null) {
                    if (p0.isNotEmpty()) {
                        validKodePerusahaan(p0.toString())
                    }
                }
            }

        })
    }


    private fun validKodePerusahaan(code : String){
        val parameter = HashMap<String, String>()
        parameter["company_code"] = "$code"
        RetrofitClient.instance.getKodePerusahaan(parameter).enqueue(object : Callback<RegisterCompanyCheck> {
            override fun onResponse(call: Call<RegisterCompanyCheck>, response: Response<RegisterCompanyCheck>) {
                if (response.isSuccessful) {
                    if (response.code() == 200) {


                        companyId = response.body()?.data!!.id
                        binding.kodeperusahaanContainer.isHelperTextEnabled = true
                        binding.kodeperusahaanContainer.helperText = "Kode Perusahaan Ditemukan"

                    } else {
//                       Toast.makeText(this@RegisterActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("error", "onResponse: ${response.message()}" )
                    binding.kodeperusahaanContainer.helperText = "Kode Perusahaan Tidak Ditemukan"
//                   Toast.makeText(this@RegisterActivity, response.code().toString() , Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(call: Call<RegisterCompanyCheck>, t: Throwable) {
                Log.e("test", "onFailure: " + t.message )
                Toast.makeText(this@RegisterActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })

    }


    private fun showBottomSheetDialogFragment() {
        val tvJobDesk = findViewById<TextView>(R.id.tvJobDeskDepartement)
        tvJobDesk.setOnClickListener {
            if (companyId != 0) {
                bottomSheetDialogFragment.companyId = companyId
                bottomSheetDialogFragment.show(supportFragmentManager, "")
            } else {
                Toast.makeText(this@RegisterActivity, "Masukan Kode Perusahaan dengan Benar", Toast.LENGTH_SHORT).show()
            }

        }

        bottomSheetDialogFragment.listener = {
            tvJobDesk.setText(it.departementTitle)
            departementId = it.departementId
        }
    }



    private fun matchPasswordListener() {
        binding.etKonfirmasiPassword.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0 != null) {
                    if (p0.isNotEmpty()) {
                        if (!etMasukanPassword.text.toString().isEmpty() && !etKonfirmasiPassword.text.toString().isEmpty()) {

                            if (etMasukanPassword.text.toString() == etKonfirmasiPassword.text.toString()) {
                                binding.konfirmPassContainer.isHelperTextEnabled = true
                                binding.konfirmPassContainer.helperText = "Password Oke"
                            } else {
                                binding.konfirmPassContainer.helperText = "Password Tidak Sama"
                            }

                        }
                    }
//
                }
            }

        })

    }

    private fun postDaftar() {

        binding.btnDaftar.setOnClickListener {

            val kodePerusahaan = binding.etKodePerusahaan.text.toString()
            val idKaryawaan = binding.etIdKaryawan.text.toString()
            val namaLengkap = binding.etNamaLengkap.text.toString()
            val email = binding.etEmail.text.toString()
            val jobDeskDepartement = binding.tvJobDeskDepartement.text.toString()
            val nomorTelepon = binding.etNomorTelepon.text.toString()
            val password = binding.etMasukanPassword.text.toString()
            val konfirmasiPass = binding.etKonfirmasiPassword.text.toString()

            if (kodePerusahaan.isEmpty()) {
                etKodePerusahaan.error = getString(R.string.error)
                etKodePerusahaan.requestFocus()
                return@setOnClickListener
            }

            if (idKaryawaan.isEmpty()) {
                etIdKaryawan.error = getString(R.string.error)
                etIdKaryawan.requestFocus()
                return@setOnClickListener
            }

            if (namaLengkap.isEmpty()) {
                etNamaLengkap.error = getString(R.string.error)
                etNamaLengkap.requestFocus()
                return@setOnClickListener
            }

            if (email.isEmpty()) {
                etEmail.error = getString(R.string.error)
                etEmail.requestFocus()
                return@setOnClickListener
            }

            if (jobDeskDepartement.isEmpty()) {
                tvJobDeskDepartement.error = getString(R.string.error)
                tvJobDeskDepartement.requestFocus()
                return@setOnClickListener

            }

            if (nomorTelepon.isEmpty()) {
                etNomorTelepon.error = getString(R.string.error)
                etNomorTelepon.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                etMasukanPassword.error = getString(R.string.error)
                etMasukanPassword.requestFocus()
                return@setOnClickListener
            }

            if (konfirmasiPass.isEmpty()) {
                etKonfirmasiPassword.error = getString(R.string.error)
                etKonfirmasiPassword.requestFocus()
                return@setOnClickListener
            }


            RetrofitClient.instance.postDaftar(RegisterParam(
                companyId = companyId,
                employeeDepartmentId = departementId,
                employeeEmail = "${etEmail.text}",
                employeeFullname = "${etNamaLengkap.text}",
                employeeNik = "${etIdKaryawan.text}",
                employeePassword = "${etMasukanPassword.text}",
                employeePhoneNo = "${etNomorTelepon.text}"),
                "Bearer ${sharedPref.tokenSplash}")
            .enqueue(object  : Callback<RegisterResponse> {
                override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                    if (response.isSuccessful) {
                        if (response.code() == 200) {

                            Toast.makeText(this@RegisterActivity, "${response.body()?.message}", Toast.LENGTH_SHORT).show()
                            onBackPressed()

                        } else {
                            Toast.makeText(this@RegisterActivity,response.body()!!.statusCode, Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@RegisterActivity, "${response.body()?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    Log.e("testError", "onFailure: ${t.message}" )
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()

                }
            })

        }

    }


}










