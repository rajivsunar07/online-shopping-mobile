package com.rajivsunar.hamroshopwear.repository


import com.rajivsunar.hamroshopwear.api.MyApiRequest
import com.rajivsunar.hamroshopwear.api.ProductAPI
import com.rajivsunar.hamroshopwear.api.ServiceBuilder
import com.rajivsunar.hamroshopwear.response.ProductResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.Part
import retrofit2.http.Path

class ProductRepository : MyApiRequest() {
    private val productApi =
        ServiceBuilder.buildService(ProductAPI::class.java)

    suspend fun getAll(): ProductResponse {
        return apiRequest {
            productApi.getAll()
        }
    }

    suspend fun getOne(id: String): ProductResponse {
        return apiRequest {
            productApi.getOne(id)
        }
    }

    suspend fun getForUser(): ProductResponse {
        return apiRequest {
            productApi.getForUser()
        }
    }

    suspend fun addProduct(
        image: ArrayList<MultipartBody.Part>,
        name: RequestBody,
        description: RequestBody,
        price: Int,
        _for: RequestBody
    ): ProductResponse {
        return apiRequest {
            productApi.addProduct(
                image,
                name,
                description,
                price,
                _for
            )
        }
    }


    suspend fun updateProduct(
        id: String,
        newImages: ArrayList<MultipartBody.Part>,
        image: ArrayList<String?>?,
        name: RequestBody,
        description: RequestBody,
        price: Int,
        _for: RequestBody
    ): ProductResponse {
        return apiRequest {
            productApi.updateProduct(
                id,
                newImages,
                image,
                name,
                description,
                price,
                _for
            )
        }
    }

    suspend fun deleteProduct(
        id: String

    ): ProductResponse {
        return apiRequest {
            productApi.deleteProduct(
                id
            )
        }
    }
}