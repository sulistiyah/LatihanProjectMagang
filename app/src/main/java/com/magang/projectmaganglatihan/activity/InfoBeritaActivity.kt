package com.magang.projectmaganglatihan.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.magang.projectmaganglatihan.adapter.InfoBeritaAdapter
import com.magang.projectmaganglatihan.adapter.ListJobDeskAdapter
import com.magang.projectmaganglatihan.api.RetrofitClient
import com.magang.projectmaganglatihan.databinding.ActivityInfoBeritaBinding
import com.magang.projectmaganglatihan.model.InfoBeritaResponse
import com.magang.projectmaganglatihan.model.RegisterDepartementListResponse
import com.magang.projectmaganglatihan.storage.SharedPrefManager
import kotlinx.android.synthetic.main.activity_info_berita.*
import kotlinx.android.synthetic.main.item_info_berita.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InfoBeritaActivity : AppCompatActivity() {

    private lateinit var binding : ActivityInfoBeritaBinding

    private lateinit var infoBeritaAdapter: InfoBeritaAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private var list: List<InfoBeritaResponse.Data> = listOf()
//    lateinit var  listener : InformasiBeritaAdapter.OnAdapterListenerInfo
    private lateinit var sharedPref: SharedPrefManager
//    var listener : ((InfoBeritaResponse.Data) -> Unit?)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoBeritaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = SharedPrefManager(this)
        layoutManager = LinearLayoutManager(this)

        backPage()
        showListInfo()
        actionClick()

    }


    private fun backPage() {
        binding.backTop.setOnClickListener {
            onBackPressed()
        }
    }



    private fun showListInfo() {

        val parameter = HashMap<String, String>()
        parameter["companyId"] = sharedPref.companyId

        RetrofitClient.instance.getListInfoBerita(parameter, "Bearer ${sharedPref.tokenLogin}")
            .enqueue(object : Callback<InfoBeritaResponse> {
                override fun onResponse(call: Call<InfoBeritaResponse>, response: Response<InfoBeritaResponse>) {
                    if (response.isSuccessful) {
                        if (response.code() == 200) {

//                            SharedPrefManager.getInstance(this@InfoBeritaActivity).saveInfoId(response.body()?.data.)

                            rvInfoBerita.setHasFixedSize(true)
                            showData(response.body()!!)
//                            infoBeritaAdapter = InfoBeritaAdapter(list)
                            rvInfoBerita.adapter = infoBeritaAdapter
                            rvInfoBerita.layoutManager = layoutManager
                            infoBeritaAdapter.notifyDataSetChanged()

                        } else {
                            Toast.makeText(this@InfoBeritaActivity,response.code(), Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@InfoBeritaActivity, "${response.body()?.message}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<InfoBeritaResponse>, t: Throwable) {
                    Log.e("listInfo", "onFailure: " + t.message )
                    Toast.makeText(this@InfoBeritaActivity, t.message, Toast.LENGTH_SHORT).show()
                }

            })

    }

    private fun showData( data : InfoBeritaResponse) {
        val results = data.data
        infoBeritaAdapter.setData(results)
    }

    private fun actionClick() {
        infoBeritaAdapter = InfoBeritaAdapter(listOf(), object : InfoBeritaAdapter.OnAdapterListener {
            override fun onClik(result: InfoBeritaResponse.Data) {
                val intent = Intent(this@InfoBeritaActivity, DetailInfoActivity::class.java)
                intent.putExtra("title", result.title)
                intent.putExtra("subTitle", result.subTitle)
                intent.putExtra("image", result.imageUrl)
                intent.putExtra("content", result.content)
                startActivity(intent)
//                startActivity(
//                    Intent(this@InfoBeritaActivity, DetailInfoActivity::class.java)
//                        .putExtra("title", result.title)
//                        .putExtra("subTitle", result.subTitle)
//                        .putExtra("image", result.imageUrl)
//                        .putExtra("content", result.content)
//                )
            }
        })

//        infoBeritaAdapter.setOnClickItemListener(object : InfoBeritaAdapter.OnItemClickListener {
//            override fun onItemClick(item: View, position: Int) {
//                var intent = Intent(this@InfoBeritaActivity, DetailInfoActivity::class.java)
//                intent.putExtra("title", infoBeritaAdapter.getData().get(position), tit)
//                intent.putExtra("subTitle", infoBeritaAdapter.getData().get(position), tvSubTitle)
//            }
//        })

//        infoBeritaAdapter.onItemClick = {
//            val intent = Intent(this, DetailInfoActivity::class.java)
//            intent.putExtra("listDetail", it)
//            intent.putExtra("title", infoBeritaAdapter.getData().ti)
//            startActivity(intent)
//        }

    }



}