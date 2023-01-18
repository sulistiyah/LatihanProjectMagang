package com.magang.projectmaganglatihan.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.magang.projectmaganglatihan.adapter.ListTugasKerjaAdapter
import com.magang.projectmaganglatihan.api.RetrofitClient
import com.magang.projectmaganglatihan.databinding.ActivityListTugasKerjaBinding
import com.magang.projectmaganglatihan.model.ListTugasKerjaResponse
import com.magang.projectmaganglatihan.storage.SharedPrefManager
import kotlinx.android.synthetic.main.activity_list_tugas_kerja.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListTugasKerjaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListTugasKerjaBinding

    private lateinit var listtugasKerjaAdapter: ListTugasKerjaAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private var list: List<ListTugasKerjaResponse.Data> = listOf()
    private lateinit var sharedPref: SharedPrefManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListTugasKerjaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = SharedPrefManager(this)
        layoutManager = LinearLayoutManager(this)

        backPage()
        getListDaftarTugas()
        actionClick()

    }

    private fun backPage() {
        binding.backTop.setOnClickListener {
            onBackPressed()
        }
    }


    private fun getListDaftarTugas() {

        val parameter = HashMap<String, String>()
        parameter["companyId"] = sharedPref.companyId

        RetrofitClient.instance.getListTugasKerja(parameter, "Bearer ${sharedPref.tokenLogin}")
            .enqueue(object : Callback<ListTugasKerjaResponse> {
                override fun onResponse(call: Call<ListTugasKerjaResponse>, response: Response<ListTugasKerjaResponse>) {
                    if (response.isSuccessful) {
                        if (response.code() == 200) {

                            rvTugasKerja.setHasFixedSize(true)
                            showData(response.body()!!)
//                            infoBeritaAdapter = InfoBeritaAdapter(list)
                            rvTugasKerja.adapter = listtugasKerjaAdapter
                            rvTugasKerja.layoutManager = layoutManager
                            listtugasKerjaAdapter.notifyDataSetChanged()

                        } else {
                            Toast.makeText(this@ListTugasKerjaActivity,response.body()!!.statusCode, Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@ListTugasKerjaActivity, "${response.body()?.message}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ListTugasKerjaResponse>, t: Throwable) {
                    Log.e("listTugasKerja", "onFailure: " + t.message )
                    Toast.makeText(this@ListTugasKerjaActivity, t.message, Toast.LENGTH_SHORT).show()
                }

            })

    }

    private fun showData( data : ListTugasKerjaResponse) {
        val results = data.data
        listtugasKerjaAdapter.setData(results)
    }

    private fun actionClick() {
        listtugasKerjaAdapter = ListTugasKerjaAdapter(listOf(), object : ListTugasKerjaAdapter.OnAdapterListener {
            override fun onClik(result: ListTugasKerjaResponse.Data) {
                val intent = Intent(this@ListTugasKerjaActivity, TugasKerjaActivity::class.java)
                startActivity(intent)
            }
        })
    }


}