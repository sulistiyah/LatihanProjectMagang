package com.magang.projectmaganglatihan.model


import com.google.gson.annotations.SerializedName

class ListTugasKerjaResponse(
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
        @SerializedName("company_project_description")
        var companyProjectDescription: String,
        @SerializedName("company_project_id")
        var companyProjectId: Int,
        @SerializedName("company_project_logo")
        var companyProjectLogo: String,
        @SerializedName("company_project_name")
        var companyProjectName: String,
        @SerializedName("company_project_status")
        var companyProjectStatus: String,
        @SerializedName("status")
        var status: Int,
        @SerializedName("status_text")
        var statusText: String
    )
}