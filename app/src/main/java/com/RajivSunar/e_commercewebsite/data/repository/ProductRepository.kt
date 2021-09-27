package com.RajivSunar.e_commercewebsite.data.repository

import com.RajivSunar.e_commercewebsite.data.api.MyApiRequest
import com.RajivSunar.e_commercewebsite.data.api.ProductAPI
import com.RajivSunar.e_commercewebsite.data.api.ServiceBuilder
import com.RajivSunar.e_commercewebsite.data.response.ProductResponse

class ProductRepository: MyApiRequest() {
    private val productApi =
        ServiceBuilder.buildService(ProductAPI::class.java)

    suspend fun getAll(): ProductResponse {
        return apiRequest {
            productApi.getAll()
        }
    }


}