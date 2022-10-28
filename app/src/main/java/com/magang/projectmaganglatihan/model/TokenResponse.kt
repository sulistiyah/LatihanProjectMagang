package com.magang.projectmaganglatihan.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep


class TokenResponse(
    @SerializedName("Data")
    var `data`: Data,
    @SerializedName("Message")
    var message: String,
    @SerializedName("StatusCode")
    var statusCode: Int
) {
    class Data(
        @SerializedName("token")
        var token: String?
    )
}