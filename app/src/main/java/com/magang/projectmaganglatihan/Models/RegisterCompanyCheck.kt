package com.magang.projectmaganglatihan.Models

import com.google.gson.annotations.SerializedName

data class RegisterCompanyCheck(

    @SerializedName("id")
    val id : Int,

    @SerializedName("company_code")
    val companyCode : String,

    @SerializedName("company_address")
    val companyAddress : String,

    @SerializedName("company_phone")
    val companyPhone : Int

)
