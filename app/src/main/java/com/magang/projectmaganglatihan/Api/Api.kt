package com.magang.projectmaganglatihan.Api

import com.magang.projectmaganglatihan.Models.LoginParam
import com.magang.projectmaganglatihan.Models.LoginResponse
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
    fun postDaftar()

//    @GET("posts/{id}/comments")
//    fun getComment(@Path("id") postId: Int): Call<ArrayList<CommentResponse>>

    @GET("v1/company_check")
    fun getKodePerusahaan()

    @GET("v1/departement_list")
    fun getJobDeskDapartemen()

}