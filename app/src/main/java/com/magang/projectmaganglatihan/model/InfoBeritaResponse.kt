package com.magang.projectmaganglatihan.model


import com.google.gson.annotations.SerializedName

class InfoBeritaResponse(
    @SerializedName("Data")
    var `data`: List<Data>,
    @SerializedName("Message")
    var message: String,
    @SerializedName("StatusCode")
    var statusCode: Int
) {
    class Data(
        @SerializedName("company_id")
        var companyId: Int,
        @SerializedName("content")
        var content: String,
        @SerializedName("created_at")
        var createdAt: String,
        @SerializedName("created_by")
        var createdBy: Int,
        @SerializedName("deleted_at")
        var deletedAt: Any,
        @SerializedName("end_date")
        var endDate: String,
        @SerializedName("image_url")
        var imageUrl: String,
        @SerializedName("info_id")
        var infoId: Int,
        @SerializedName("start_date")
        var startDate: String,
        @SerializedName("sub_title")
        var subTitle: String,
        @SerializedName("tanggal")
        var tanggal: String,
        @SerializedName("tanggal_berakhir")
        var tanggalBerakhir: String,
        @SerializedName("tanggal_mulai")
        var tanggalMulai: String,
        @SerializedName("title")
        var title: String,
        @SerializedName("updated_at")
        var updatedAt: String
    )
}