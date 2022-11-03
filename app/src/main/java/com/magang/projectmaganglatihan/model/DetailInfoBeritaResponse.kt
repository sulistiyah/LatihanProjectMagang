package com.magang.projectmaganglatihan.model


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName


class DetailInfoBeritaResponse(
    @SerializedName("Data")
    var `data`: Data,
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
        @SerializedName("image_url")
        var imageUrl: String,
        @SerializedName("info_id")
        var infoId: Int,
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
//        : Parcelable {
//        constructor(parcel: Parcel) : this(
//            parcel.readInt(),
//            parcel.readString()!!
//        )
//
//        override fun writeToParcel(parcel: Parcel, flags: Int) {
//            parcel.writeInt(image)
//            parcel.writeString(name)
//        }
//
//        override fun describeContents(): Int {
//            return 0
//        }
//
//        companion object CREATOR : Parcelable.Creator<Data> {
//            override fun createFromParcel(parcel: Parcel): Data {
//                return Data(parcel)
//            }
//
//            override fun newArray(size: Int): Array<Data?> {
//                return arrayOfNulls(size)
//            }
//        }
//    }

}