package com.RajivSunar.e_commercewebsite.data.repository

import com.RajivSunar.e_commercewebsite.data.api.ExchangeProductAPI
import com.RajivSunar.e_commercewebsite.data.api.MyApiRequest
import com.RajivSunar.e_commercewebsite.data.api.ServiceBuilder
import com.RajivSunar.e_commercewebsite.data.response.ExchangeProductResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part

class ExchangeProductRepository : MyApiRequest() {
    private val exchangeProductAPI =
        ServiceBuilder.buildService(ExchangeProductAPI::class.java)

    suspend fun createExchangeProduct(
        image: MultipartBody.Part,
        name: RequestBody,
        description: RequestBody,
        exchangeFor: RequestBody,
        seller: RequestBody
    ): ExchangeProductResponse {
        return apiRequest {
            exchangeProductAPI.createExchangeProduct(
                image,
                name,
                description,
                exchangeFor,
                seller
            )
        }
    }

    suspend fun updateExchangeProduct(
        id: String,
        status: String
    ): ExchangeProductResponse {
        return apiRequest {
            exchangeProductAPI.updateExchangeProduct(
                id,
                status
            )
        }
    }

    suspend fun deleteExchangeProduct(
        id: String
    ): ExchangeProductResponse {
        return apiRequest {
            exchangeProductAPI.deleteExchangeProduct(
                id
            )
        }
    }

    suspend fun getExchangeProducts(
        _for: String
    ): ExchangeProductResponse {
        return apiRequest {
            exchangeProductAPI.getExchangeProducts(
                _for
            )
        }
    }



}