package com.RajivSunar.e_commercewebsite

import com.RajivSunar.e_commercewebsite.data.api.ServiceBuilder
import com.RajivSunar.e_commercewebsite.data.repository.CheckoutRepository
import com.RajivSunar.e_commercewebsite.data.repository.ExchangeProductRepository
import com.RajivSunar.e_commercewebsite.data.repository.UserRepository
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.Test

class CheckoutUnitTest {

    @Test
    fun checkInsert() = runBlocking {
        val userRepository = UserRepository()
        val loginResponse = userRepository.login("test@email.com","test1")
        ServiceBuilder.token = "Bearer ${loginResponse.token}"


        val repository = CheckoutRepository()
        val response = repository.insert(
            "Kathmandu",
            "9384728484"
        )
        val expectedResult = true
        val actualResult = response.success
        TestCase.assertEquals(expectedResult, actualResult)
    }
}