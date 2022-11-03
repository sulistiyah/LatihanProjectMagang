package com.magang.projectmaganglatihan.model
import com.google.gson.annotations.SerializedName


class EditProfilResponse(
    @SerializedName("Data")
    var `data`: Data,
    @SerializedName("Message")
    var message: String,
    @SerializedName("StatusCode")
    var statusCode: Int
) {
    class Data
}