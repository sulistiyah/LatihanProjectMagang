package com.magang.projectmaganglatihan.api

import android.content.Context
import com.magang.projectmaganglatihan.model.*
import com.magang.projectmaganglatihan.storage.SharedPrefManager
import retrofit2.Call
import retrofit2.http.*

interface Api {

    @Headers("Accept: application/json")
    @POST("v1/staff/authenticate")
    fun userLogin(@Body loginParam: LoginParam,
    @Header("Authorization")token:String
                  ) : Call<LoginResponse>

    @Headers(
        "Accept: application/json",
        "X-APP-TOKEN: TWTpknTux7PzuqDh6qLJQPXNvRT3an7B"
    )
    @POST("v1/auth/token")
    fun token(@Body tokenParam : TokenParam ) : Call<TokenResponse>

    @Headers("Accept: application/json")
    @POST("v1/register")
    fun postDaftar(@Body registerParam: RegisterParam): Call<RegisterResponse>


    @GET("v1/company_check")
    fun getKodePerusahaan(
        @QueryMap parameters : HashMap<String, String>
    ): Call<RegisterCompanyCheck>

    @GET("v1/departement_list")
    fun getJobDeskDapartement(
        @QueryMap parameter: HashMap<String, Int>
    ): Call<RegisterDepartementListResponse>


}