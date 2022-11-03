package com.magang.projectmaganglatihan.model

import com.google.gson.annotations.SerializedName

class RegisterDepartementListResponse(
    @SerializedName("Data")
    var `data`: ArrayList<Data>,
    @SerializedName("Message")
    var message: String,
    @SerializedName("StatusCode")
    var statusCode: Int
)
     {
    class Data(
        @SerializedName("company_id")
        var companyId: Int,
        @SerializedName("created_at")
        var createdAt: Any,
        @SerializedName("deleted_at")
        var deletedAt: Any,
        @SerializedName("departement_created_by")
        var departementCreatedBy: Int,
        @SerializedName("departement_description")
        var departementDescription: String,
        @SerializedName("departement_id")
        var departementId: Int,
        @SerializedName("departement_title")
        var departementTitle: String,
        @SerializedName("departement_updated_by")
        var departementUpdatedBy: Int,
        @SerializedName("is_active")
        var isActive: Int,
        @SerializedName("updated_at")
        var updatedAt: Any
    )
}