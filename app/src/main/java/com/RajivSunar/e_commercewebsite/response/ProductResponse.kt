package com.RajivSunar.e_commercewebsite.response

import com.RajivSunar.e_commercewebsite.entity.Product
import com.google.gson.JsonArray
import org.json.JSONObject

data class ProductResponse (
    val success: Boolean? = null,
    val result: List<Product>? = null
)