package com.RajivSunar.e_commercewebsite.data.response

import com.RajivSunar.e_commercewebsite.data.entity.Order

data class OrderResponse (
    val success: Boolean? = null,
    val result: List<Order>? = null,
    val message: String? = null
    )

