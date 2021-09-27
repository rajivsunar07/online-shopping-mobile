package com.RajivSunar.e_commercewebsite.data.api

import com.RajivSunar.e_commercewebsite.data.entity.User
import com.RajivSunar.e_commercewebsite.data.response.UserResponse
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