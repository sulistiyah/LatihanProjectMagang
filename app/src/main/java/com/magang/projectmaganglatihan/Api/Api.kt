package com.magang.projectmaganglatihan.api

import com.magang.projectmaganglatihan.model.*
import retrofit2.Call
import retrofit2.http.*

interface Api {

    @POST("login")
    fun userLogin(@Body loginParam: LoginParam) : Call<LoginResponse>
//
//    @GET("users")
//    fun getUsers(
//        @QueryMap parameters : HashMap<String, String>):Call<UsersResponse>



    @Headers(
        "Accept: application/json",
        "Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJhdXRoLXNlcnZpY2UiLCJzdWIiOjEsImlhdCI6MTY2NDc4Mzk2NywiZXhwIjoxNjY0ODcwMzY3LCJzY29wZSI6ImRldmVsb3BtZW50IiwicGxhdGZvcm0iOiJhbmRyb2lkIiwiY29tcGFueV9pZCI6MSwiZGF0YSI6eyJwbGF0Zm9ybSI6ImFuZHJvaWQifX0.QbHdvYDBy0p2HV7EVEQ-TXWCwkWP4Ygv_8_PoOZzaf4"
    )
    @POST("v1/register")
    fun postDaftar(@Body registerParam: RegisterParam): Call<RegisterResponse>


    @GET("v1/company_check")
    fun getKodePerusahaan(
        @QueryMap parameters : HashMap<String, String>
    ): Call<RegisterCompanyCheck>

    @GET("v1/departement_list/{company_id}")
    fun getJobDeskDapartemen(
        @Path("company_id") company_id: String
    ): Call<ArrayList<RegisterDepartementList>>


}