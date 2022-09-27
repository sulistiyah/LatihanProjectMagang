package com.magang.projectmaganglatihan.model

import com.google.gson.annotations.SerializedName

data class RegisterResponse (

    @SerializedName("company_id")
    val kodePerusahaan : ArrayList<RegisterCompanyCheck>,

    @SerializedName("employee_nik")
    val idKaryawan : Int,

    @SerializedName("employee_fullname")
    val namaLengkap : String,

    @SerializedName("employee_email")
    val email : String,

    @SerializedName("employee_department_id")
    val jobDeskDapartemen : ArrayList<RegisterDepartementList>,

    @SerializedName("employee_phone_no")
    val noTelepon : Int,

    @SerializedName("employee_password")
    val password : String


)