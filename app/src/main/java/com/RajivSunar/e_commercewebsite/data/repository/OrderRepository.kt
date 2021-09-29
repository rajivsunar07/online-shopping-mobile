package com.RajivSunar.e_commercewebsite.data.repository

import com.RajivSunar.e_commercewebsite.data.api.MyApiRequest
import com.RajivSunar.e_commercewebsite.data.api.OrderAPI
import com.RajivSunar.e_commercewebsite.data.api.ServiceBuilder
import com.RajivSunar.e_commercewebsite.data.response.OrderResponse

class OrderRepository : MyApiRequest() {
    private val orderAPI =
        ServiceBuilder.buildService(OrderAPI::class.java)

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

    suspend fun getCart(): OrderResponse{
        return apiRequest {
            orderAPI.getCart()
        }
    }

    suspend fun updateOrder(_id: String, status: String) : OrderResponse{
        return apiRequest{
            orderAPI.updateOrder(_id, status)
        }
    }
}