package com.magang.projectmaganglatihan.Models

import com.google.gson.annotations.SerializedName

data class RegisterDepartementList(

    @SerializedName("departement_id")
    val departementId : Int,

    @SerializedName("company_id")
    val companyId : Int,

    @SerializedName("departement_title")
    val departementTitle : String

)
