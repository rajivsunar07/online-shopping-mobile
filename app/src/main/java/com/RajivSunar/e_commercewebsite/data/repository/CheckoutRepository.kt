package com.RajivSunar.e_commercewebsite.data.repository

import com.RajivSunar.e_commercewebsite.data.api.CheckoutAPI
import com.RajivSunar.e_commercewebsite.data.api.MyApiRequest
import com.RajivSunar.e_commercewebsite.data.api.ServiceBuilder
import com.RajivSunar.e_commercewebsite.data.entity.Checkout
import com.RajivSunar.e_commercewebsite.data.response.CheckoutResponse

class CheckoutRepository: MyApiRequest() {
    private val checkoutAPI =
        ServiceBuilder.buildService(CheckoutAPI::class.java)

    suspend fun insert(address: String, phone: String): CheckoutResponse {
        return apiRequest {
            checkoutAPI.insert(address, phone)
        }
    }
}