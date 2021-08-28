package com.RajivSunar.e_commercewebsite.repository

import com.RajivSunar.e_commercewebsite.api.MyApiRequest
import com.RajivSunar.e_commercewebsite.api.ProductAPI
import com.RajivSunar.e_commercewebsite.api.ServiceBuilder
import com.RajivSunar.e_commercewebsite.api.UserAPI
import com.RajivSunar.e_commercewebsite.db.ProductDB
import com.RajivSunar.e_commercewebsite.db.UserDB
import com.RajivSunar.e_commercewebsite.entity.Product
import com.RajivSunar.e_commercewebsite.entity.User
import com.RajivSunar.e_commercewebsite.response.ProductResponse
import com.RajivSunar.e_commercewebsite.response.UserResponse

class ProductRepository: MyApiRequest() {
    private val productApi =
        ServiceBuilder.buildService(ProductAPI::class.java)

    suspend fun getAll(): ProductResponse {
        return apiRequest {
            productApi.getAll()
        }
    }


}