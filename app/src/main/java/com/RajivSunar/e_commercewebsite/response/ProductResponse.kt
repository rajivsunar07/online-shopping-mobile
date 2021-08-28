package com.RajivSunar.e_commercewebsite.response

import com.RajivSunar.e_commercewebsite.entity.Product

data class ProductResponse (
    val success: Boolean? = null,
    val result: List<Product>? = null
)