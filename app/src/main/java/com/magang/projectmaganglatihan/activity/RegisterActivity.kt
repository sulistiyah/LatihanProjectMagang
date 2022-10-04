package com.magang.projectmaganglatihan.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.magang.projectmaganglatihan.R
import com.magang.projectmaganglatihan.api.RetrofitClient
import com.magang.projectmaganglatihan.model.RegisterDepartementList
import com.magang.projectmaganglatihan.model.RegisterResponse
import com.magang.projectmaganglatihan.adapter.ListJobDeskAdapter
import com.magang.projectmaganglatihan.databinding.ActivityRegisterBinding
import com.magang.projectmaganglatihan.model.RegisterCompanyCheck
import com.magang.projectmaganglatihan.model.RegisterParam
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegisterBinding

    private lateinit var dialog: BottomSheetDialog
    private lateinit var recyclerView: RecyclerView
    private lateinit var listJobDeskAdapter : ListJobDeskAdapter
    private var listJobDesk: ArrayList<RegisterDepartementList> = arrayListOf()
    private var companyCode = 0
//    private lateinit var companyCheck : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        kodePerusahaanFocusListener()

        binding.backTop.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btnDaftar.setOnClickListener {
            val kodePerusahaan = binding.etKodePerusahaan.text.toString()
            val idKaryawaan = binding.etIdKaryawan.text.toString()
            val namaLengkap = binding.etNamaLengkap.text.toString()
            val email = binding.etEmail.text.toString()
            val jobDeskDepartement = binding.etJobDeskDepartement.text.toString()
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
                etJobDeskDepartement.error = getString(R.string.error)
                etJobDeskDepartement.requestFocus()
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

            val registerParam = RegisterParam(
                companyId = 1,
                employeeDepartmentId = 1,
                employeeEmail = "sulis@gmail.com",
                employeeFullname = "Sulis Tiyah",
                employeeNik = "987654321",
                employeePassword = "12345",
                employeePhoneNo = "081292323052"

            )


            RetrofitClient.instance.postDaftar(registerParam)
                .enqueue(object  : Callback<RegisterResponse> {
                override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                    if (response.isSuccessful) {
                        if (response.code() == 200) {
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
                    Log.e("testError", "onFailure: ${t.message}" )
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()

                }
            })

//            binding.kodeperusahaanContainer.helperText = validKodePerusahaan()

            showBottomSheet()

        }


    }

    private fun showBottomSheet() {
        val dialogView = layoutInflater.inflate(R.layout.bottom_sheet,null)
        dialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
        dialog.setContentView(dialogView)
        recyclerView = dialogView.findViewById(R.id.rvItemJobDesk)
        dialog.show()

        RetrofitClient.instance.getJobDeskDapartemen("1").enqueue(object : Callback<RegisterDepartementList>{
            override fun onResponse(
                call: Call<RegisterDepartementList>,
                response: Response<RegisterDepartementList>
            ) {
                listJobDeskAdapter = ListJobDeskAdapter(listJobDesk)
                recyclerView.adapter = listJobDeskAdapter
            }

            override fun onFailure(call: Call<RegisterDepartementList>, t: Throwable) {
                Toast.makeText(this@RegisterActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
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
//        binding.etKodePerusahaan.setOnFocusChangeListener { _, focused ->
//            if(!focused)
//            {
//                binding.kodeperusahaanContainer.helperText = validKodePerusahaan()
//            }
//        }
    }


    private fun validKodePerusahaan(code : String){
        val parameter = HashMap<String, String>()
            parameter["company_code"] = "$code"
        RetrofitClient.instance.getKodePerusahaan(parameter).enqueue(object : Callback<RegisterCompanyCheck> {
            override fun onResponse(call: Call<RegisterCompanyCheck>, response: Response<RegisterCompanyCheck>) {
               if (response.isSuccessful) {
                   if (response.code() == 200) {
                       companyCode = response.body()?.data!!.id
                       binding.kodeperusahaanContainer.isHelperTextEnabled = true
                       binding.kodeperusahaanContainer.helperText = "Kode Perusahaan Ditemukan"
                   } else {
                       Toast.makeText(this@RegisterActivity, response.code(), Toast.LENGTH_SHORT).show()
                   }
               } else {
                   Toast.makeText(this@RegisterActivity, response.errorBody().toString(), Toast.LENGTH_SHORT).show()
               }

            }

            override fun onFailure(call: Call<RegisterCompanyCheck>, t: Throwable) {
                Log.e("test", "onFailure: " + t.message )
            }

        })

//        val codeText = binding.etKodePerusahaan.text.toString()
//        if(!codeText.matches("codelabs".toRegex())) {
//            return "Kode Perusahaan Tidak Ditemukan"
//        }
//        return "Kode Perusahaan Ditemukan"
    }

//    fun listJobDesk(view: View) {
//        val onClickJobDesk = AlertDialog.Builder(this)
//        onClickJobDesk.setTitle("Job Desk")
//
//        onClickJobDesk.setItems(listJobDeskAdapter) {dialog, which ->
//            Toast.makeText(applicationContext, listJobDeskAdapter[which], Toast.LENGTH_SHORT).show()
//        }
//
//        val dialog = onClickJobDesk.create()
//        dialog.show()
//    }


//    private fun getJobDeskDepartement() {
//        rv_listJobDesk.setHasFixedSize(true)
//        rv_listJobDesk.layoutManager = layoutManager
//        listJobDeskAdapter = ListJobDeskAdapter(listJobDesk)
//        rv_listJobDesk.adapter = registerAdapter
//        etJobDeskDepartement.setOnClickListener {
//            val parameter = HashMap<String, String>()
//            parameter["company_id"] = "1"
//            RetrofitClient.instance.getJobDeskDapartemen(parameter).enqueue(object : Callback<ArrayList<RegisterDepartementList>>{
//                override fun onResponse(call: Call<ArrayList<RegisterDepartementList>>, response: Response<ArrayList<RegisterDepartementList>>) {
//                    val listDepartementResponse = response.body()
//                    if (listDepartementResponse != null) {
//                        registerAdapter.addListJobDesk(listDepartementResponse)
//                    }
//                }
//
//                override fun onFailure(call: Call<ArrayList<RegisterDepartementList>>, t: Throwable) {
//
//                }
//
//            })
//        }
//
//    }

}

