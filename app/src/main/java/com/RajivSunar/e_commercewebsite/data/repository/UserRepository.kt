package com.RajivSunar.e_commercewebsite.data.repository

import com.RajivSunar.e_commercewebsite.data.api.MyApiRequest
import com.RajivSunar.e_commercewebsite.data.api.ServiceBuilder
import com.RajivSunar.e_commercewebsite.data.api.UserAPI
import com.RajivSunar.e_commercewebsite.data.entity.User
import com.RajivSunar.e_commercewebsite.data.response.UserResponse

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