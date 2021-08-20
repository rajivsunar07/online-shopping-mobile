package com.RajivSunar.e_commercewebsite.api

import com.RajivSunar.e_commercewebsite.entity.User
import com.RajivSunar.e_commercewebsite.response.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserAPI {

    @POST("register")
    suspend fun register(
        @Body user: User
    ): Response<UserResponse>
}