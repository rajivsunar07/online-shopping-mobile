package com.RajivSunar.e_commercewebsite

import android.net.Uri
import com.RajivSunar.e_commercewebsite.data.api.ServiceBuilder
import com.RajivSunar.e_commercewebsite.data.entity.User
import com.RajivSunar.e_commercewebsite.data.repository.UserRepository
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.junit.Test
import java.io.File


class UserUnitTest {
    @Test
    fun checkRegister() = runBlocking {
        val user = User("test@email.com","test","test")
        val repository = UserRepository()
        val response = repository.register(user)
        val expectedResult = true
        val actualResult = response.success
        TestCase.assertEquals(expectedResult, actualResult)
    }

    @Test
    fun checkLogin() = runBlocking {
        val repository = UserRepository()
        val response = repository.login("test@email.com","test")
        val expectedResult = true
        val actualResult = response.success
        TestCase.assertEquals(expectedResult, actualResult)
    }

    @Test
    fun checkGetUser() = runBlocking {
        val repository = UserRepository()
        val loginResponse = repository.login("test@email.com","test")
        ServiceBuilder.token = "Bearer ${loginResponse.token}"

        val response = repository.getUser()
        val expectedResult = "test@email.com"
        val actualResult = response.user?.email
        TestCase.assertEquals(expectedResult, actualResult)
    }

    @Test
    fun checkUpdateUser() = runBlocking {
        val repository = UserRepository()
        val loginResponse = repository.login("test@email.com","test")
        ServiceBuilder.token = "Bearer ${loginResponse.token}"

        val email = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "test@email.com")

        val file = File(
            "src/test/java/com/RajivSunar/e_commercewebsite/images/testlogo.png"
        )
        val filePart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "image",
            file.name,
            RequestBody.create("image/*".toMediaTypeOrNull(), file)
        )

        val response = repository.updateUser( filePart, email=email, null, null, null)
        val expectedResult = true
        val actualResult = response.success
        TestCase.assertEquals(expectedResult, actualResult)
    }

    @Test
    fun checkPasswordChange() = runBlocking {
        val repository = UserRepository()
        val loginResponse = repository.login("test@email.com","test")
        ServiceBuilder.token = "Bearer ${loginResponse.token}"

        val response = repository.changePassword("test", "test1")
        val expectedResult = true
        val actualResult = response.success
        TestCase.assertEquals(expectedResult, actualResult)
    }
}