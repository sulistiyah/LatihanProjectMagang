package com.magang.projectmaganglatihan.model
import com.google.gson.annotations.SerializedName


class RegisterCompanyCheck(
    @SerializedName("Data")
    var `data`: Data,
    @SerializedName("Message")
    var message: String,
    @SerializedName("StatusCode")
    var statusCode: Int
) {
    class Data(
        @SerializedName("company_address")
        var companyAddress: String,
        @SerializedName("company_code")
        var companyCode: String,
        @SerializedName("company_image")
        var companyImage: String,
        @SerializedName("company_image_alt")
        var companyImageAlt: String,
        @SerializedName("company_phone")
        var companyPhone: String,
        @SerializedName("created_at")
        var createdAt: Any,
        @SerializedName("deleted_at")
        var deletedAt: Any,
        @SerializedName("id")
        var id: Int,
        @SerializedName("updated_at")
        var updatedAt: Any
    )
}