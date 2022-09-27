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


    @POST("v1/register")
    fun postDaftar(): Call<RegisterResponse>


    @GET("v1/company_check")
    fun getKodePerusahaan(
        @QueryMap parameters : HashMap<String, String>
    ): Call<ArrayList<RegisterCompanyCheck>>


    @GET("v1/departement_list/{company_id}")
    fun getJobDeskDapartemen(
        @Path("company_id") company_id: String
    ): Call<ArrayList<RegisterDepartementList>>


}