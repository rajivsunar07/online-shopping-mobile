package com.rajivsunar.hamroshopwear.response

import com.rajivsunar.hamroshopwear.entity.Order


data class OrderResponse (
    val success: Boolean? = null,
    val result: List<Order?>? = null,
    val message: String? = null
    )

