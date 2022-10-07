package com.magang.projectmaganglatihan.model

import com.google.gson.annotations.SerializedName

data class LoginParam (
    @SerializedName("username")
    var username : String?,
    @SerializedName("password")
    var password: String?)