package com.RajivSunar.e_commercewebsite.data.api

import com.RajivSunar.e_commercewebsite.data.response.CommentResponse
import retrofit2.Response
import retrofit2.http.*

interface CommentAPI {

    @GET("comment/{product}")
    suspend fun getAll(
        @Path("product") product: String
    ): Response<CommentResponse>

    @FormUrlEncoded
    @POST("comment/")
    suspend fun addComment(
        @Field("product") product: String,
        @Field("description") description: String
    ): Response<CommentResponse>

    @DELETE("comment/{id}")
    suspend fun deleteComment(
        @Path("id") id: String
    ): Response<CommentResponse>
}