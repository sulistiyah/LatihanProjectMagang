package com.magang.projectmaganglatihan.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.magang.projectmaganglatihan.R
import com.magang.projectmaganglatihan.adapter.ListJobDeskAdapter
import com.magang.projectmaganglatihan.adapter.ListJobDeskAdapter.OnAdapterListener
import com.magang.projectmaganglatihan.api.RetrofitClient
import com.magang.projectmaganglatihan.databinding.FragmentJobDeskBottomSheetBinding
import com.magang.projectmaganglatihan.model.RegisterDepartementListResponse
import com.magang.projectmaganglatihan.model.RegisterDepartementListResponse.Data
import com.magang.projectmaganglatihan.storage.SharedPrefManager
import kotlinx.android.synthetic.main.fragment_job_desk_bottom_sheet.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class JobDeskBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentJobDeskBottomSheetBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedPref: SharedPrefManager
    var companyId = 0
    private lateinit var listJobDeskAdapter: ListJobDeskAdapter
    lateinit var layoutManager: LinearLayoutManager
    var listener : ((Data) -> Unit)? = null



    override fun getTheme(): Int {
        return R.style.BottomSheetDialogTheme
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentJobDeskBottomSheetBinding.inflate(inflater, container, false)

        sharedPref = SharedPrefManager(requireActivity())

        showListJobDesk()
        actionClick()

        return binding.root
    }

    private fun showListJobDesk() {

        sharedPref = SharedPrefManager(requireActivity())
        layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL


        val parameter = HashMap<String, Int>()
        parameter["company_id"] = 1
        RetrofitClient.instance.getJobDeskDapartement(parameter).enqueue(object : Callback<RegisterDepartementListResponse> {
            override fun onResponse(call: Call<RegisterDepartementListResponse>, response: Response<RegisterDepartementListResponse>) {
                if (response.isSuccessful) {
                    if (response.code() == 200) {


                        rvItemJobDesk.setHasFixedSize(true)
                        showData(response.body()!!)
//                        listJobDeskAdapter = ListJobDeskAdapter(response.body()?.data!!, listener)
                        rvItemJobDesk.adapter = listJobDeskAdapter
                        rvItemJobDesk.layoutManager = layoutManager
                        listJobDeskAdapter.notifyDataSetChanged()



                    } else {
                        Toast.makeText(context, response.body()!!.statusCode, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RegisterDepartementListResponse>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }

        })

    }

    private fun showData( data : RegisterDepartementListResponse) {
        val results = data.data
        listJobDeskAdapter.setData(results)
    }


    private fun actionClick() {

        listJobDeskAdapter = ListJobDeskAdapter(arrayListOf(), object : OnAdapterListener {
            override fun onClik(result: Data) {

                listener?.invoke(result)
                SharedPrefManager.getInstance(context?.applicationContext!!).saveDepartementId(result.departementId.toString())
                this@JobDeskBottomSheetFragment.dismiss()

            }
        })

    }



}