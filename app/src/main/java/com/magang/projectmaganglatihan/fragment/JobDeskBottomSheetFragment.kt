package com.magang.projectmaganglatihan.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.magang.projectmaganglatihan.R
import com.magang.projectmaganglatihan.adapter.ListJobDeskAdapter
import com.magang.projectmaganglatihan.api.RetrofitClient
import com.magang.projectmaganglatihan.databinding.FragmentJobDeskBottomSheetBinding
import com.magang.projectmaganglatihan.model.RegisterDepartementList
import com.magang.projectmaganglatihan.model.RegisterDepartementListResponse
import kotlinx.android.synthetic.main.fragment_job_desk_bottom_sheet.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class JobDeskBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentJobDeskBottomSheetBinding? = null
    private val binding get() = _binding!!

    var companyId = 0
    private lateinit var listJobDeskAdapter: ListJobDeskAdapter
    private var list : ArrayList<RegisterDepartementListResponse.Data> = arrayListOf()
    lateinit var layoutManager: LinearLayoutManager
    lateinit var  recyclerView: RecyclerView



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentJobDeskBottomSheetBinding.inflate(inflater, container, false)


        layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL


        val parameter = HashMap<String, Int>()
        parameter["company_id"] = 1
        RetrofitClient.instance.getJobDeskDapartement(parameter).enqueue(object : Callback<RegisterDepartementListResponse> {
            override fun onResponse(call: Call<RegisterDepartementListResponse>, response: Response<RegisterDepartementListResponse>) {
                if (response.isSuccessful) {
                    if (response.code() == 200) {
                        rvItemJobDesk.setHasFixedSize(true)
                        listJobDeskAdapter = ListJobDeskAdapter(list)
                        rvItemJobDesk.adapter = listJobDeskAdapter
                        rvItemJobDesk.layoutManager = layoutManager
                        listJobDeskAdapter.notifyDataSetChanged()

                    } else {
                        Toast.makeText(context, response.body()?.message, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, response.code().toString() , Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RegisterDepartementListResponse>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }

        })



        return binding.root
    }

    override fun getTheme(): Int {
        return R.style.BottomSheetDialogTheme
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


}