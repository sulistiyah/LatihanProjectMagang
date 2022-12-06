package com.magang.projectmaganglatihan.model


import com.google.gson.annotations.SerializedName

class AttributeParam(
    @SerializedName("data")
    var `data`: Data,
    @SerializedName("employee_id")
    var employeeId: Int
) {
    class Data(
        @SerializedName("color")
        var color: Int,
        @SerializedName("crop_image_url")
        var cropImageUrl: String,
        @SerializedName("distance")
        var distance: Double,
        @SerializedName("extra")
        var extra: List<List<Double>>,
        @SerializedName("id")
        var id: String,
        @SerializedName("location")
        var location: Location,
        @SerializedName("title")
        var title: String
    ) {
        class Location(
            @SerializedName("bottom")
            var bottom: Double,
            @SerializedName("left")
            var left: Double,
            @SerializedName("right")
            var right: Double,
            @SerializedName("top")
            var top: Double
        )
    }
}