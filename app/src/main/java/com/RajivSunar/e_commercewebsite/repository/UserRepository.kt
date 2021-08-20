package com.RajivSunar.e_commercewebsite.repository

import com.RajivSunar.e_commercewebsite.api.MyApiRequest
import com.RajivSunar.e_commercewebsite.api.ServiceBuilder
import com.RajivSunar.e_commercewebsite.api.UserAPI
import com.RajivSunar.e_commercewebsite.entity.User
import com.RajivSunar.e_commercewebsite.response.UserResponse

class UserRepository: MyApiRequest() {
    private val userApi =
        ServiceBuilder.buildService(UserAPI::class.java)

    suspend fun register(user: User): UserResponse{
        return apiRequest {
            userApi.register(user)
        }
    }

    suspend fun login(username: String, password: String): UserResponse{
        return apiRequest {
            userApi.login(username, password)
        }
    }
}