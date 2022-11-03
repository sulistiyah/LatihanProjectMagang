package com.magang.projectmaganglatihan.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.magang.projectmaganglatihan.R
import com.magang.projectmaganglatihan.api.RetrofitClient
import com.magang.projectmaganglatihan.databinding.ActivityDetailInfoBinding
import com.magang.projectmaganglatihan.model.DetailInfoBeritaResponse
import com.magang.projectmaganglatihan.model.InfoBeritaResponse
import com.magang.projectmaganglatihan.storage.SharedPrefManager
import kotlinx.android.synthetic.main.activity_detail_info.*
import kotlinx.android.synthetic.main.activity_detail_info.imgGambar
import kotlinx.android.synthetic.main.activity_detail_info.tvTitle
import kotlinx.android.synthetic.main.activity_info_berita.*
import kotlinx.android.synthetic.main.item_info_berita.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailInfoActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailInfoBinding

    private lateinit var sharedPref: SharedPrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = SharedPrefManager(this)

        backPage()
        showDetail()

    }

    private fun backPage() {
        binding.backTop.setOnClickListener {
            onBackPressed()
        }
    }

    private fun showDetail() {

        val parameter = HashMap<String, String>()
        parameter["companyId"] = sharedPref.companyId
        parameter["info_id"] = sharedPref.infoId

        RetrofitClient.instance.getDetailListInfo(parameter, "Bearer ${sharedPref.tokenLogin}")
            .enqueue(object : Callback<DetailInfoBeritaResponse> {
                override fun onResponse(call: Call<DetailInfoBeritaResponse>, response: Response<DetailInfoBeritaResponse>) {
                    if (response.isSuccessful) {
                        if (response.code() == 200) {

                            binding.tvTitle.text = intent.getStringExtra("title")
                            binding.tvSubTitle.text = intent.getStringExtra("subTitle")
                            binding.tvContent.text = intent.getStringExtra("content")
                            Glide.with(this@DetailInfoActivity)
                                .load(intent.getStringExtra("image"))
                                .placeholder(R.drawable.img_placeholder)
                                .error(R.drawable.img_placeholder)
                                .into(imgGambar)


                        } else {
                            Toast.makeText(this@DetailInfoActivity,response.code(), Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@DetailInfoActivity, "${response.body()?.message}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<DetailInfoBeritaResponse>, t: Throwable) {
                    Log.e("detailListInfo", "onFailure: " + t.message )
                    Toast.makeText(this@DetailInfoActivity, t.message, Toast.LENGTH_SHORT).show()
                }

            })




    }



}