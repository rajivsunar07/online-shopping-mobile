package com.RajivSunar.e_commercewebsite.data.repository

import com.RajivSunar.e_commercewebsite.data.api.CommentAPI
import com.RajivSunar.e_commercewebsite.data.api.MyApiRequest
import com.RajivSunar.e_commercewebsite.data.api.ServiceBuilder
import com.RajivSunar.e_commercewebsite.data.response.CommentResponse

class CommentRepository: MyApiRequest() {
    private val commentAPI =
        ServiceBuilder.buildService(CommentAPI::class.java)

    suspend fun getAll(product: String): CommentResponse {
        return apiRequest {
            commentAPI.getAll(product)
        }
    }

    suspend fun addCommment(product: String, description: String): CommentResponse {
        return apiRequest {
            commentAPI.addComment(product, description)
        }
    }

    suspend fun deleteComment(id: String): CommentResponse {
        return apiRequest {
            commentAPI.deleteComment(id)
        }
    }
}