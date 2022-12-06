package com.magang.projectmaganglatihan.api


import com.magang.projectmaganglatihan.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface Api {

    //Login
    @Headers("Accept: application/json")
    @POST("v1/staff/authenticate")
    fun userLogin(
        @Body loginParam: LoginParam,
        @Header("Authorization") token:String
    ) : Call<LoginResponse>


    //Token
    @Headers(
        "Accept: application/json",
        "X-APP-TOKEN: TWTpknTux7PzuqDh6qLJQPXNvRT3an7B"
    )
    @POST("v1/auth/token")
    fun token(@Body tokenParam : TokenParam ) : Call<TokenResponse>


    //Register
    @Headers("Accept: application/json")
    @POST("v1/register")
    fun postDaftar(
        @Body registerParam: RegisterParam,
        @Header("Authorization") token:String
    ): Call<RegisterResponse>


    //Kode Perusahaan - Register
    @GET("v1/company_check")
    fun getKodePerusahaan(
        @QueryMap parameters : HashMap<String, String>
    ): Call<RegisterCompanyCheck>

    //Job Desk - Register
    @GET("v1/departement_list")
    fun getJobDeskDapartement(
        @QueryMap parameter: HashMap<String, Int>
    ): Call<RegisterDepartementListResponse>

    //SetDataWajah - Home Activity
    @Multipart
    @Headers("Accept: application/json")
    @POST("v1/member/attribute")
    fun postSetDataWajah(
        @Header("Authorization") token: String,
        @Part ("employee_id") employeeId: RequestBody
    ): Call<SetDataWajahResponse>


    //Absen - HomeActivity
    @Headers("Accept: application/json")
    @POST("v1/member/save-attribute")
    fun postAbsenSekarang(
        @Header("Authorization") token:String,
        @Body attributeParam: AttributeParam
    ): Call<AttributeResponse>


    //ListKehadiran - Home Activity
    @Headers("Accept: application/json")
    @GET("v1/member/attendance/list")
    fun getListKehadiran(
        @Header("Authorization") token: String,
        @QueryMap parameter : HashMap<String, String>
    ): Call<SetDataWajahResponse>




    //My Profile - Profil
    @Headers("Accept: application/json")
    @GET("v1/member/me")
    fun getMyProfile(
        @QueryMap parameter : HashMap<String, String>,
        @Header("Authorization") token: String
    ): Call<MyProfileResponse>

    //Update Avatar - ProfilActivity
    @Multipart
    @Headers("Accept: application/json")
    @POST("v1/member/update_my_avatar")
    fun postAvatar(
        @Header("Authorization") token: String,
        @Part image : MultipartBody.Part,
        @Part ("employee_id") employeeId : RequestBody,
        @Part ("company_id") companyId : RequestBody
    ): Call<UpdateAvatarResponse>


    //Edit Profil
    @Headers("Accept: application/json")
    @POST("v1/member/me")
    fun postEditProfil(
        @Header("Authorization") token: String,
        @Field("company_id") companyId : String,
        @Field("employee_id") employeeId : String,
        @Field("employee_name") employeeName : String,
        @Field("departement_id") departementId : String,
        @Field("phone_no") phoneNo : String
    ): Call<MyProfileResponse>


    //Informasi Berita - HomeActivity
    @Headers("Accept: application/json")
    @GET("v1/info-list")
    fun getListInfoBerita(
        @QueryMap parameter : HashMap<String, String>,
        @Header("Authorization") token: String
    ): Call<InfoBeritaResponse>

    //Detail Informasi Berita - InfoBeritaActivity
    @Headers("Accept: application/json")
    @GET("v1/info-detail")
    fun getDetailListInfo(
        @QueryMap parameter : HashMap<String, String>,
        @Header("Authorization") token: String
    ): Call<DetailInfoBeritaResponse>


}