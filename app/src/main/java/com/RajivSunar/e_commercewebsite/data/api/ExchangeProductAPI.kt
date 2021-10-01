package com.RajivSunar.e_commercewebsite.data.api

import com.RajivSunar.e_commercewebsite.data.response.ExchangeProductResponse
import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ExchangeProductAPI {
    @Multipart
    @POST("exchangeProduct/")
    suspend fun createExchangeProduct(
        @Part image: MultipartBody.Part,
        @Part("name") name: RequestBody,
        @Part("description") description: RequestBody,
        @Part("exchangeFor") exchangeFor: RequestBody,
        @Part("seller") seller: RequestBody
    ): Response<ExchangeProductResponse>

    @FormUrlEncoded
    @PATCH("exchangeProduct/{id}")
    suspend fun updateExchangeProduct(
        @Path("id") id: String,
        @Field("status") status: String
    ): Response<ExchangeProductResponse>

    @DELETE("exchangeProduct/{id}")
    suspend fun deleteExchangeProduct(
        @Path("id") id: String
    ): Response<ExchangeProductResponse>

    @GET("exchangeProduct/{for}")
    suspend fun getExchangeProducts(
        @Path("for") _for: String
    ): Response<ExchangeProductResponse>

}