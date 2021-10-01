package com.RajivSunar.e_commercewebsite.data.response

import com.RajivSunar.e_commercewebsite.data.entity.Comment

class CommentResponse(
    val success: Boolean? = null,
    val result: List<Comment>? = null,
    val message: String? = null
)
