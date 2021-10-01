package com.RajivSunar.e_commercewebsite.data.response

import com.RajivSunar.e_commercewebsite.data.entity.Product

data class ProductResponse (
    val success: Boolean? = null,
    val result: List<Product>? = null,
    val message: String? = null
)