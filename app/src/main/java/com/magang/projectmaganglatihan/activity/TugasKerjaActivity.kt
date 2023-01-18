package com.magang.projectmaganglatihan.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.magang.projectmaganglatihan.R
import com.magang.projectmaganglatihan.adapter.ListTugasKerjaAdapter
import com.magang.projectmaganglatihan.adapter.TugasKerjaAdapter
import com.magang.projectmaganglatihan.databinding.ActivityListTugasKerjaBinding
import com.magang.projectmaganglatihan.databinding.ActivityTugasKerjaBinding
import com.magang.projectmaganglatihan.model.ListTugasKerjaResponse
import com.magang.projectmaganglatihan.model.TugasKerjaResponse
import com.magang.projectmaganglatihan.storage.SharedPrefManager

class TugasKerjaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTugasKerjaBinding

    private lateinit var tugasKerjaAdapter: TugasKerjaAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private var list: List<TugasKerjaResponse.Data> = listOf()
    private lateinit var sharedPref: SharedPrefManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTugasKerjaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = SharedPrefManager(this)
        layoutManager = LinearLayoutManager(this)

        backPage()
        getTugasKerja()

    }

    private fun backPage() {
        binding.backTop.setOnClickListener {
            onBackPressed()
        }
    }

    private fun getTugasKerja() {



    }


}