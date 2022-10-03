package com.magang.projectmaganglatihan.model

import com.google.gson.annotations.SerializedName

class TokenParam (
    @SerializedName("client")
    var client : String,

    @SerializedName("secret")
    var secret : String
    )
