package com.RajivSunar.e_commercewebsite

import com.RajivSunar.e_commercewebsite.data.api.ServiceBuilder
import com.RajivSunar.e_commercewebsite.data.repository.CommentRepository
import com.RajivSunar.e_commercewebsite.data.repository.ProductRepository
import com.RajivSunar.e_commercewebsite.data.repository.UserRepository
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.w3c.dom.Comment

class CommentUnitTest {

    @Test
    fun checkGetAll() = runBlocking {

        val repository = CommentRepository()
        val response = repository.getAll("6157f0324484320582bac641")

        val expectedResult = true
        val actualResult = response.success
        TestCase.assertEquals(expectedResult, actualResult)
    }

    @Test
    fun checkAddComment() = runBlocking {
        val userRepository = UserRepository()
        val loginResponse = userRepository.login("test@email.com","test1")
        ServiceBuilder.token = "Bearer ${loginResponse.token}"

        val repository = CommentRepository()
        val response = repository.addCommment("6157f0324484320582bac641", "new comment")

        val expectedResult = true
        val actualResult = response.success
        TestCase.assertEquals(expectedResult, actualResult)
    }

    @Test
    fun checkDeleteComment() = runBlocking {
        val userRepository = UserRepository()
        val loginResponse = userRepository.login("test@email.com","test1")
        ServiceBuilder.token = "Bearer ${loginResponse.token}"

        val repository = CommentRepository()
        val response = repository.deleteComment("61587f47663483174d654f01")

        val expectedResult = true
        val actualResult = response.success
        TestCase.assertEquals(expectedResult, actualResult)
    }





}