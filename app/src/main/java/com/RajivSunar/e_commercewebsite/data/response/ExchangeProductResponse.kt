package com.RajivSunar.e_commercewebsite.data.response

import com.RajivSunar.e_commercewebsite.data.entity.ExchangeProduct

data class ExchangeProductResponse (
    val success: Boolean? = null,
    val result: List<ExchangeProduct?>? = null,
    val message: String? = null
)