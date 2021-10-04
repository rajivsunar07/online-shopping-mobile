package com.rajivsunar.hamroshopwear.response

import com.rajivsunar.hamroshopwear.entity.User


class UserResponse (
    val success: Boolean? = null,
    val token: String? = null,
    val user: User? = null,
    val message: String? = null
)