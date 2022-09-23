package com.magang.projectmaganglatihan.Api

import com.magang.projectmaganglatihan.Models.LoginParam
import com.magang.projectmaganglatihan.Models.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.QueryMap

interface Api {
    @POST("login")
    fun userLogin(@Body loginParam: LoginParam) : Call<LoginResponse>
//
//    @GET("users")
//    fun getUsers(
//        @QueryMap parameters : HashMap<String, String>):Call<UsersResponse>
}