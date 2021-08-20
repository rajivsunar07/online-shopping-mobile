package com.RajivSunar.e_commercewebsite.api

import com.RajivSunar.e_commercewebsite.entity.User
import com.RajivSunar.e_commercewebsite.response.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface UserAPI {

    @POST("user/register")
    suspend fun register(
        @Body user: User
    ): Response<UserResponse>

    @FormUrlEncoded
    @POST("user/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<UserResponse>
}