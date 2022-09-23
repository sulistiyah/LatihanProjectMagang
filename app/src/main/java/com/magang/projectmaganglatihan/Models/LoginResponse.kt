package com.magang.projectmaganglatihan.Models

import com.google.gson.annotations.SerializedName

data class LoginResponse (

    @SerializedName("token")
    var token : String?,
    @SerializedName ("error")
    var error : String?)

