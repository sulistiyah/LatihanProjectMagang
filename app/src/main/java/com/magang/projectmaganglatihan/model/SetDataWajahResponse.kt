package com.magang.projectmaganglatihan.model


import com.google.gson.annotations.SerializedName

class SetDataWajahResponse(
    @SerializedName("Data")
    var `data`: Data,
    @SerializedName("Message")
    var message: String,
    @SerializedName("StatusCode")
    var statusCode: Int
) {
    class Data(
        @SerializedName("created_at")
        var createdAt: String,
        @SerializedName("employee_id")
        var employeeId: Int,
        @SerializedName("id")
        var id: Int,
        @SerializedName("object")
        var objectX: Object,
        @SerializedName("updated_at")
        var updatedAt: String
    ) {
        class Object(
            @SerializedName("color")
            var color: Int,
            @SerializedName("crop_image_url")
            var cropImageUrl: String,
            @SerializedName("distance")
            var distance: Int,
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
                var bottom: Int,
                @SerializedName("left")
                var left: Int,
                @SerializedName("right")
                var right: Int,
                @SerializedName("top")
                var top: Int
            )
        }
    }
}