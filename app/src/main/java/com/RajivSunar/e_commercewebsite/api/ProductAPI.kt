package com.RajivSunar.e_commercewebsite.api

import com.RajivSunar.e_commercewebsite.entity.Product
import com.RajivSunar.e_commercewebsite.entity.User
import com.RajivSunar.e_commercewebsite.response.ProductResponse
import com.RajivSunar.e_commercewebsite.response.UserResponse
import retrofit2.Response
import retrofit2.http.*

interface ProductAPI {

    @GET("product/")
    suspend fun getAll(): Response<ProductResponse>

}