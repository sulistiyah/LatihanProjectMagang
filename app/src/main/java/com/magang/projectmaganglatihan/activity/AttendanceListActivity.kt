package com.magang.projectmaganglatihan.activity

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.setFragmentResult
import com.magang.projectmaganglatihan.R
import com.magang.projectmaganglatihan.databinding.ActivityAttendanceListBinding
import com.magang.projectmaganglatihan.fragment.DatePickerFragment
import com.magang.projectmaganglatihan.storage.SharedPrefManager
import kotlinx.android.synthetic.main.activity_attendance_list.*
import java.text.SimpleDateFormat
import java.util.*

class AttendanceListActivity :AppCompatActivity() {
    private lateinit var binding : ActivityAttendanceListBinding
    private lateinit var sharedPrefManager: SharedPrefManager
    private lateinit var btnkalender : Button
    private lateinit var tvkalender: TextView
    private lateinit var datePickerFragment:DatePickerFragment


    override fun onCreate(savedInstancestate: Bundle?) {
        super.onCreate(savedInstancestate)
        binding = ActivityAttendanceListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        btnkalender = findViewById(R.id.imgDown)
        tvkalender = findViewById(R.id.tvTanggalkalender)
        


        sharedPrefManager = SharedPrefManager(this)

        backPage()
//        datePickerdialog()
    }
    private fun backPage() {
        binding.back.setOnClickListener {
            onBackPressed()
        }
    }



//    private fun datePickerdialog() {
//        val dropDown = findViewById<ImageView>(R.id.imgDown)
//        dropDown.setOnClickListener {
//            datePickerFragment.show(supportFragmentManager, "")
//
//            val tanggal = findViewById<TextView>(R.id.tvTanggalkalender)
//            tanggal.setText(datePickerFragment.setFragmentResult())
//        }
//    }
////
////    dateAttendanceFocusListener()
//
//
//    }
//    private fun onViewCreated(view: View){
//        _binding = ActivityAttendanceListBinding.bind(view)
//    }


//    private fun dateAttendanceFocusListener() {
//        binding.tvTanggalkalender.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//
//            }
//
//            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//
//            }
//
//            override fun afterTextChanged(p0: Editable?) {
//                if (p0 != null) {
//                    if (p0.isNotEmpty()) {
//                        AttendanceList(p0.toString())
//                    }
//                }
//            }
//        })
//    }
//
//    //
//    private fun AttendanceList(date: String) {
//        val parameter = HashMap<String, String>()
//        parameter["company_id"] = sharedPrefManager.companyId
//        parameter["date"] = "$date"
//
//        RetrofitClient.instance.getAttendanceList(parameter)
//            .enqueue(object : Callback<AttendanceListResponse> {
//                override fun onResponse(call: Call<AttendanceListResponse>,
//                    response: Response<AttendanceListResponse>) {
//                    if (response.isSuccessful){
//                        if(response.code() == 200){
//
//
//                            companyId = response.body()?.data!!.totalAttendanceAbsence
//                            companyId = response.body()?.data!!.totalAttendancePermit
//                            companyId = response.body()?.data!!.totalAttendanceSick
//                            companyId = response.body()?.data!!.totalAttendanceSuccess
//
//
//                        }
//
//                    }
//                }
//
//                override fun onFailure(call: Call<AttendanceListResponse>, t: Throwable) {
//                    Log.e("test","OnFailure:" + t.message)
//                    Toast.makeText(this@AttendanceListActivity, t.message,
//                        Toast.LENGTH_SHORT).show()
//                }
//            })
//    }

}