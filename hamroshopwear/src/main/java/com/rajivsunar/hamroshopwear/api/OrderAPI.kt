package com.rajivsunar.hamroshopwear.api


import com.rajivsunar.hamroshopwear.response.OrderResponse
import retrofit2.Response
import retrofit2.http.*

interface OrderAPI {
//
//    @GET("order/")
//    suspend fun getAll(): Response<OrderResponse>

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

    @FormUrlEncoded
    @PATCH("order/{id}")
    suspend fun updateOrder(
        @Path("id") id: String,
        @Field("status") status: String?,
        @Field("total_price") total_price: Int?
    ): Response<OrderResponse>

    @FormUrlEncoded
    @PATCH("order/item/{itemId}")
    suspend fun updateOrderItem(
        @Path("itemId") itemid: String,
        @Field("price") price: Int?,
        @Field("quantity") quantity: Int?,
    ): Response<OrderResponse>

    @DELETE("order/item/{itemId}")
    suspend fun deleteOrderItem(@Path("itemId") itemId: String): Response<OrderResponse>

}