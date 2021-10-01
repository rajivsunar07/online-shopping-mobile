package com.RajivSunar.e_commercewebsite.data.response

import com.RajivSunar.e_commercewebsite.data.entity.User

class UserResponse (
    val success: Boolean? = null,
    val token: String? = null,
    val user: User? = null,
    val message: String? = null
)