package com.magang.projectmaganglatihan.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.magang.projectmaganglatihan.api.RetrofitClient
import com.magang.projectmaganglatihan.model.RegisterDepartementList
import com.magang.projectmaganglatihan.model.RegisterResponse
import com.magang.projectmaganglatihan.adapter.ListJobDeskAdapter
import com.magang.projectmaganglatihan.databinding.ActivityRegisterBinding
import com.magang.projectmaganglatihan.storage.SharedPrefManager
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.item_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.nio.file.attribute.AclEntry

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegisterBinding

    private lateinit var sharedPrefManager: SharedPrefManager
    private lateinit var layoutManager: LinearLayoutManager
    private var showPass = false
    private var showKonfirmasiPass = false
    private lateinit var registerAdapter: ListJobDeskAdapter
    private lateinit var listJobDeskAdapter : ListJobDeskAdapter
    private var listJobDesk: ArrayList<RegisterDepartementList> = arrayListOf()
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
                etKodePerusahaan.error = "Tidak Boleh Kosog"
                etKodePerusahaan.requestFocus()
                return@setOnClickListener
            }

            if (idKaryawaan.isEmpty()) {
                etIdKaryawan.error = "Tidak Boleh Kosog"
                etIdKaryawan.requestFocus()
                return@setOnClickListener
            }

            if (namaLengkap.isEmpty()) {
                etNamaLengkap.error = "Tidak Boleh Kosong"
                etNamaLengkap.requestFocus()
                return@setOnClickListener
            }

            if (email.isEmpty()) {
                etEmail.error = "Tidak Boleh Kosog"
                etEmail.requestFocus()
                return@setOnClickListener
            }

            if (jobDeskDepartement.isEmpty()) {
                etJobDeskDepartement.error = "Tidak Boleh Kosog"
                etJobDeskDepartement.requestFocus()
                return@setOnClickListener
            }

            if (nomorTelepon.isEmpty()) {
                etNomorTelepon.error = "Tidak Boleh Kosog"
                etNomorTelepon.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                etMasukanPassword.error = "Tidak Boleh Kosog"
                etMasukanPassword.requestFocus()
                return@setOnClickListener
            }

            if (konfirmasiPass.isEmpty()) {
                etKonfirmasiPassword.error = "Tidak Boleh Kosog"
                etKonfirmasiPassword.requestFocus()
                return@setOnClickListener
            }


            RetrofitClient.instance.postDaftar().enqueue(object  : Callback<RegisterResponse> {
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
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()

                }
            })

            binding.kodeperusahaanContainer.helperText = validKodePerusahaan()

            binding.etJobDeskDepartement.setOnClickListener {
                rv_listJobDesk.setHasFixedSize(true)
                rv_listJobDesk.layoutManager = LinearLayoutManager(this)

                RetrofitClient.instance.getJobDeskDapartemen("v1/departement_list/1").enqueue(object : Callback<ArrayList<RegisterDepartementList>>{
                    override fun onResponse(
                        call: Call<ArrayList<RegisterDepartementList>>,
                        response: Response<ArrayList<RegisterDepartementList>>
                    ) {
                        response.body()?.let { listJobDesk.addAll(it) }
                        val adapterJobDesk = ListJobDeskAdapter(listJobDesk)
                        rv_listJobDesk.adapter = adapterJobDesk
                    }

                    override fun onFailure(call: Call<ArrayList<RegisterDepartementList>>, t: Throwable) {

                    }

                })
            }

        }


    }

    private fun kodePerusahaanFocusListener() {
        binding.etKodePerusahaan.setOnFocusChangeListener { _, focused ->
            if(!focused)
            {
                binding.kodeperusahaanContainer.helperText = validKodePerusahaan()
            }
        }
    }


    private fun validKodePerusahaan(): String {
        val codeText = binding.etKodePerusahaan.text.toString()
        if(!codeText.matches("codelabs".toRegex())) {
            return "Kode Perusahaan Tidak Ditemukan"
        }
        return "Kode Perusahaan Ditemukan"
    }

//    fun listJobeDesk(view: View) {
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
//            val paramater = HashMap<String, String>()
//            paramater["company_id"] = "1"
//            RetrofitClient.instance.getJobDeskDapartemen(paramater).enqueue(object : Callback<ArrayList<RegisterDepartementList>>{
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

