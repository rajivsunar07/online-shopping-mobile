package com.RajivSunar.e_commercewebsite.data.response

import com.RajivSunar.e_commercewebsite.data.entity.Checkout
import com.RajivSunar.e_commercewebsite.data.entity.Order

data class CheckoutResponse (
    val success: Boolean? = null,
    val result: Checkout? = null,
    val message: String? = null
)