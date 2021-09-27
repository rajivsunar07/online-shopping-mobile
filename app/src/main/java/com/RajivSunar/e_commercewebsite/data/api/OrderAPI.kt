package com.RajivSunar.e_commercewebsite.data.api

import com.RajivSunar.e_commercewebsite.data.response.OrderResponse
import com.RajivSunar.e_commercewebsite.data.response.UserResponse
import retrofit2.Response
import retrofit2.http.*

interface OrderAPI {

    @GET("order/")
    suspend fun getAll(): Response<OrderResponse>

    @FormUrlEncoded
    @POST("order/")
    suspend fun addToCart(
        @Field("product") product: String,
        @Field("quantity") quantity: Int,
        @Field("price") price: Int,
        @Field("seller") seller: String,
        @Field("exchangeFor") exchangeFor: String?,
        @Field("for") itemFor: String?,
        ): Response<OrderResponse>

    @GET("order/")
    suspend fun getCart(): Response<OrderResponse>
}