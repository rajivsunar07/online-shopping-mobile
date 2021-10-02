package com.RajivSunar.e_commercewebsite

import com.RajivSunar.e_commercewebsite.data.api.ServiceBuilder
import com.RajivSunar.e_commercewebsite.data.repository.ProductRepository
import com.RajivSunar.e_commercewebsite.data.repository.UserRepository
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.junit.Test
import java.io.File

class ProductUnitTest {

    @Test
    fun checkGetAll() = runBlocking {
        val repository = ProductRepository()
        val response = repository.getAll()

        val expectedResult = true
        val actualResult = response.success
        TestCase.assertEquals(expectedResult, actualResult)
    }

    @Test
    fun checkCreateProduct() = runBlocking {
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
        val price = 1000
        val _for = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "sell")

        val parts = ArrayList<MultipartBody.Part>()
        parts.add(filePart)

        val repository = ProductRepository()
        val response = repository.addProduct(
            parts,
            name,
            description,
            price,
            _for
        )
        val expectedResult = true
        val actualResult = response.success
        TestCase.assertEquals(expectedResult, actualResult)
    }


    @Test
    fun checkDeleteProduct() = runBlocking {
        val userRepository = UserRepository()
        val loginResponse = userRepository.login("test@email.com","test1")
        ServiceBuilder.token = "Bearer ${loginResponse.token}"

        val repository = ProductRepository()
        val response = repository.deleteProduct("61584d61663483174d654df4")

        val expectedResult = true
        val actualResult = response.success
        TestCase.assertEquals(expectedResult, actualResult)
    }

}