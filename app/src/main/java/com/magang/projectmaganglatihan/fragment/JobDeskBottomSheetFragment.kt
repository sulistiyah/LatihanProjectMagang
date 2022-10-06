package com.magang.projectmaganglatihan.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.magang.projectmaganglatihan.adapter.ListJobDeskAdapter
import com.magang.projectmaganglatihan.databinding.ActivityLoginBinding.inflate
import com.magang.projectmaganglatihan.databinding.FragmentJobDeskBottomSheetBinding
import com.magang.projectmaganglatihan.databinding.FragmentJobDeskBottomSheetBinding.inflate
import com.magang.projectmaganglatihan.model.RegisterDepartementList


class JobDeskBottomSheetFragment : Fragment() {

    private var _binding: FragmentJobDeskBottomSheetBinding? = null
    private val binding get() = _binding!!

    private lateinit var dialog: BottomSheetDialog
    private lateinit var recyclerView: RecyclerView
    private lateinit var listJobDeskAdapter : ListJobDeskAdapter
    private var list: ArrayList<RegisterDepartementList> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentJobDeskBottomSheetBinding.inflate(inflater, container, false)
        return binding.root


    }


}