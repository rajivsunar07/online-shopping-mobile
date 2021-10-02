package com.RajivSunar.e_commercewebsite

import com.RajivSunar.e_commercewebsite.data.api.ServiceBuilder
import com.RajivSunar.e_commercewebsite.data.repository.ExchangeProductRepository
import com.RajivSunar.e_commercewebsite.data.repository.ProductRepository
import com.RajivSunar.e_commercewebsite.data.repository.UserRepository
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.junit.Test
import java.io.File

class ExchangeProductUnitTest {

    @Test
    fun checkCreateExchangeProduct() = runBlocking {
        val userRepository = UserRepository()
        val loginResponse = userRepository.login("test@email.com","test1")
        ServiceBuilder.token = "Bearer ${loginResponse.token}"

        val file = File(
            "src/test/java/com/RajivSunar/e_commercewebsite/images/download.jpeg"
        )
        val filePart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "image",
            file.name,
            RequestBody.create("image/*".toMediaTypeOrNull(), file)
        )

        val name = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "pants")
        val description = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "this is a pant")
        val exchangeFor = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "6157f0324484320582bac641")
        val seller = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "6150ab6b1f290e42a4b9625c")


        val repository = ExchangeProductRepository()
        val response = repository.createExchangeProduct(
            filePart,
            name,
            description,
            exchangeFor,
            seller
        )
        val expectedResult = true
        val actualResult = response.success
        TestCase.assertEquals(expectedResult, actualResult)
    }

    @Test
    fun checkUpdateExchangeProduct() = runBlocking {
        val userRepository = UserRepository()
        val loginResponse = userRepository.login("test@email.com","test1")
        ServiceBuilder.token = "Bearer ${loginResponse.token}"


        val repository = ExchangeProductRepository()
        val response = repository.updateExchangeProduct(
            "615816bc75bd6a0a28ac77de",
            "accepted"
        )
        val expectedResult = true
        val actualResult = response.success
        TestCase.assertEquals(expectedResult, actualResult)
    }



    @Test
    fun checkGetExchangeProducts() = runBlocking {
        val userRepository = UserRepository()
        val loginResponse = userRepository.login("test@email.com","test1")
        ServiceBuilder.token = "Bearer ${loginResponse.token}"


        val repository = ExchangeProductRepository()
        val response = repository.getExchangeProducts(
            "user"
        )
        val expectedResult = true
        val actualResult = response.success
        TestCase.assertEquals(expectedResult, actualResult)
    }



}