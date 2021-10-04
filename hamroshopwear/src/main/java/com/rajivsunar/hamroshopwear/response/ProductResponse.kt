package com.rajivsunar.hamroshopwear.response

import com.rajivsunar.hamroshopwear.entity.Product


data class ProductResponse (
    val success: Boolean? = null,
    val result: List<Product>? = null,
    val message: String? = null
)