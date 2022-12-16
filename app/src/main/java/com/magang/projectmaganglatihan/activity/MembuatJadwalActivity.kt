package com.magang.projectmaganglatihan.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.magang.projectmaganglatihan.R
import com.magang.projectmaganglatihan.databinding.ActivityMembuatJadwalBinding

class MembuatJadwalActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMembuatJadwalBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMembuatJadwalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        backPage()
    }

    private fun backPage() {
        binding.backRencanaizin.setOnClickListener {
            onBackPressed()
        }
    }
}