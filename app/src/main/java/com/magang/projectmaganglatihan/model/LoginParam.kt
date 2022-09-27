package com.magang.projectmaganglatihan.model

import com.google.gson.annotations.SerializedName

data class LoginParam (
    @SerializedName("email")
    var email : String?,
    @SerializedName("password")
    var password: String?)