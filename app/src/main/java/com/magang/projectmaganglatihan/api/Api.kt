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
    @Multipart
    @Headers("Accept: application/json")
    @POST("v1/member/me")
    fun postEditProfil(
        @Header("Authorization") token: String,
        @Part("company_id") companyId : RequestBody,
        @Part("employee_id") employeeId : RequestBody,
        @Part("employee_name") employeeName : RequestBody,
        @Part("departement_id") departementId : RequestBody,
        @Part("phone_no") phoneNo : RequestBody
    ): Call<EditProfilResponse>


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