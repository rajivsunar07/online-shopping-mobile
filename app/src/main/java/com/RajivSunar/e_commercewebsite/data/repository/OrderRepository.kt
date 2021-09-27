package com.RajivSunar.e_commercewebsite.data.repository

import com.RajivSunar.e_commercewebsite.data.api.MyApiRequest
import com.RajivSunar.e_commercewebsite.data.api.OrderAPI
import com.RajivSunar.e_commercewebsite.data.api.ServiceBuilder
import com.RajivSunar.e_commercewebsite.data.response.OrderResponse
import retrofit2.http.Field

class OrderRepository : MyApiRequest() {
    private val orderAPI =
        ServiceBuilder.buildService(OrderAPI::class.java)

    suspend fun getAll(): OrderResponse {
        return apiRequest {
            orderAPI.getAll()
        }
    }

    suspend fun addToCart(
        product: String,
        quantity: Int,
        price: Int,
        seller: String,
        exchangeFor: String?,
        itemFor: String?,
    ): OrderResponse {
        return apiRequest {
            orderAPI.addToCart(
                product,
                quantity,
                price,
                seller,
                exchangeFor,
                itemFor,
            )
        }
    }
}