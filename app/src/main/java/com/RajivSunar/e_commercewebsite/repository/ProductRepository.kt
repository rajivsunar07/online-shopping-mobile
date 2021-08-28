package com.RajivSunar.e_commercewebsite.repository

import com.RajivSunar.e_commercewebsite.api.MyApiRequest
import com.RajivSunar.e_commercewebsite.api.ProductAPI
import com.RajivSunar.e_commercewebsite.api.ServiceBuilder
import com.RajivSunar.e_commercewebsite.response.ProductResponse

class ProductRepository: MyApiRequest() {
    private val productApi =
        ServiceBuilder.buildService(ProductAPI::class.java)

    suspend fun getAll(): ProductResponse {
        return apiRequest {
            productApi.getAll()
        }
    }


}