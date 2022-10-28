package com.magang.projectmaganglatihan.model

import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep


class TokenParam(
    @SerializedName("client")
    var client: String,
    @SerializedName("secret")
    var secret: String
)