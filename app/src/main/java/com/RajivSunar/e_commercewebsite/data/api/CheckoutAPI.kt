package com.RajivSunar.e_commercewebsite.data.api

import com.RajivSunar.e_commercewebsite.data.entity.Checkout
import com.RajivSunar.e_commercewebsite.data.response.CheckoutResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface CheckoutAPI {

    @FormUrlEncoded
    @POST("checkout/")
    suspend fun insert(
        @Field("address") address: String,
        @Field("phone") phone: String
    ): Response<CheckoutResponse>

}