package com.magang.projectmaganglatihan.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class TokenResponse(
    @SerializedName("Data")
    var `data`: Data,
    @SerializedName("Message")
    var message: String,
    @SerializedName("StatusCode")
    var statusCode: Int
) {
    @Keep
    data class Data(
        @SerializedName("token")
        var token: String
    )
}