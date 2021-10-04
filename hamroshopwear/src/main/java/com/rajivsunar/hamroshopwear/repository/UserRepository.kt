package com.rajivsunar.hamroshopwear.repository



import com.rajivsunar.hamroshopwear.api.MyApiRequest
import com.rajivsunar.hamroshopwear.api.ServiceBuilder
import com.rajivsunar.hamroshopwear.api.UserAPI
import com.rajivsunar.hamroshopwear.entity.User
import com.rajivsunar.hamroshopwear.response.UserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UserRepository: MyApiRequest() {
    private val userApi =
        ServiceBuilder.buildService(UserAPI::class.java)

    suspend fun register(user: User): UserResponse {
        return apiRequest {
            userApi.register(user)
        }
    }

    suspend fun login(username: String, password: String): UserResponse{
        return apiRequest {
            userApi.login(username, password)
        }
    }

    suspend fun getUser(): UserResponse{
        return apiRequest {
            userApi.getUser()
        }
    }

    suspend fun updateUser(
        image: MultipartBody.Part?,
        email: RequestBody?,
        name: RequestBody?,
        phone: RequestBody?,
        address: RequestBody?
    ): UserResponse{
        return apiRequest {
            userApi.updateUser(
                image = image,
                email = email,
                name = name,
                phone= phone,
                address = address
            )
        }
    }

    suspend fun changePassword(password: String, newPassword: String): UserResponse{
        return apiRequest {
            userApi.changePassword(password, newPassword)
        }
    }
}