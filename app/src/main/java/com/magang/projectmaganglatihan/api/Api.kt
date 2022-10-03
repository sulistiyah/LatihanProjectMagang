package com.magang.projectmaganglatihan.api

import android.media.session.MediaSession
import com.magang.projectmaganglatihan.model.*
import retrofit2.Call
import retrofit2.http.*

interface Api {

    @POST("v1/staff/authenticate")
    fun userLogin() : Call<LoginResponse>

    @Headers("Accept:application/json","X-APP-TOKEN:TWTpknTux7PzuqDh6qLJQPXNvRT3an7B")
    @POST("v1/auth/token")
    fun token (@QueryMap tokenParam: TokenParam):Call<TokenResponse>
//    fun token(@Body tokenParam : TokenParam ) : Call<TokenResponse>
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