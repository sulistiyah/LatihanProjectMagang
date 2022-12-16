package com.magang.projectmaganglatihan.fragment

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import java.text.SimpleDateFormat
import java.util.*

class DatePickerFragment: DialogFragment(),DatePickerDialog.OnDateSetListener {
    private val calendar = Calendar.getInstance()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(requireActivity(),this,year,month,day)
    }

    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH,month)
        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth)


        val selectedDate = SimpleDateFormat("dd-MM-yyyy",Locale.ENGLISH).format(calendar.time)
        val selectedDateBundle =Bundle()
        selectedDateBundle.putString("REQUEST_CODE",selectedDate)


        setFragmentResult("REQUEST_CODE",selectedDateBundle)
    }
}