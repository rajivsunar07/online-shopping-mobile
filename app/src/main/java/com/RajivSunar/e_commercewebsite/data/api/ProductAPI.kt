package com.RajivSunar.e_commercewebsite.data.api

import com.RajivSunar.e_commercewebsite.data.response.ProductResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ProductAPI {

    @GET("product/")
    suspend fun getAll(): Response<ProductResponse>

    @GET("product/{id}")
    suspend fun getOne(
        @Path("id") id: String
        ): Response<ProductResponse>


    @GET("product/user/all")
    suspend fun getForUser(): Response<ProductResponse>

    @Multipart
    @POST("product/")
    suspend fun addProduct(
        @Part image: ArrayList<MultipartBody.Part>,
        @Part("name") name: RequestBody,
        @Part("description") description: RequestBody,
        @Part("price") price: Int,
        @Part("for") _for: RequestBody
    ): Response<ProductResponse>
}