package com.RajivSunar.e_commercewebsite.data.api

import com.RajivSunar.e_commercewebsite.data.entity.User
import com.RajivSunar.e_commercewebsite.data.response.UserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

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

    @GET("user/")
    suspend fun getUser(): Response<UserResponse>

    @Multipart
    @PATCH("user/")
    suspend fun updateUser(
        @Part image: MultipartBody.Part,
        @Part("email") email: RequestBody,
        @Part("name") name: RequestBody,
        @Part("address") address: RequestBody,
        @Part("phone") phone: RequestBody
    ): Response<UserResponse>

    @FormUrlEncoded
    @PATCH("user/password")
    suspend fun changePassword(
        @Field("password") password: String,
        @Field("newPassword") newPassword: String
    ): Response<UserResponse>

}