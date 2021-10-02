package com.RajivSunar.e_commercewebsite

import com.RajivSunar.e_commercewebsite.data.api.ServiceBuilder
import com.RajivSunar.e_commercewebsite.data.repository.OrderRepository
import com.RajivSunar.e_commercewebsite.data.repository.ProductRepository
import com.RajivSunar.e_commercewebsite.data.repository.UserRepository
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.Test

class OrderUnitTest {

    @Test
    fun checkAddToCart() = runBlocking {
        val userRepository = UserRepository()
        val loginResponse = userRepository.login("test@email.com","test1")
        ServiceBuilder.token = "Bearer ${loginResponse.token}"

        val repository = OrderRepository()
        val response = repository.addToCart(
            "6157f0324484320582bac641",
            1,
            1000,
            "6150ab6b1f290e42a4b9625c",
            null,
            null
        )

        val expectedResult = true
        val actualResult = response.success
        TestCase.assertEquals(expectedResult, actualResult)
    }

    @Test
    fun checkGetCart() = runBlocking {
        val userRepository = UserRepository()
        val loginResponse = userRepository.login("test@email.com","test1")
        ServiceBuilder.token = "Bearer ${loginResponse.token}"

        val repository = OrderRepository()
        val response = repository.getCart()

        val expectedResult = true
        val actualResult = response.success
        TestCase.assertEquals(expectedResult, actualResult)
    }

    @Test
    fun checkUpdateOrder() = runBlocking {
        val userRepository = UserRepository()
        val loginResponse = userRepository.login("test@email.com","test1")
        ServiceBuilder.token = "Bearer ${loginResponse.token}"

        val repository = OrderRepository()
        val response = repository.updateOrder(
            "61586892663483174d654eae",
            "ordered",
            1000
        )

        val expectedResult = true
        val actualResult = response.success
        TestCase.assertEquals(expectedResult, actualResult)
    }

    @Test
    fun checkOrderItem() = runBlocking {
        val userRepository = UserRepository()
        val loginResponse = userRepository.login("test@email.com","test1")
        ServiceBuilder.token = "Bearer ${loginResponse.token}"

        val repository = OrderRepository()
        val response = repository.updateOrderItem(
            "61585dfa663483174d654e84",
            2000,
            2
        )

        val expectedResult = true
        val actualResult = response.success
        TestCase.assertEquals(expectedResult, actualResult)
    }

    @Test
    fun checkDeleteOrderItem() = runBlocking {
        val userRepository = UserRepository()
        val loginResponse = userRepository.login("test@email.com","test1")
        ServiceBuilder.token = "Bearer ${loginResponse.token}"

        val repository = OrderRepository()
        val response = repository.deleteOrderItem(
            "61586892663483174d654eac"
        )

        val expectedResult = true
        val actualResult = response.success
        TestCase.assertEquals(expectedResult, actualResult)
    }



}