package com.RajivSunar.e_commercewebsite.data.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ServiceBuilder {

    var BASE_URL = "http://10.0.2.2:5000/"
    var  token: String? = null

    var okHttpClient = OkHttpClient.Builder()
        .addInterceptor(MyInterceptor())
        .connectTimeout(1, TimeUnit.MINUTES)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()

    private val retrofitBUilder = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    fun <T> buildService(serviceType: Class<T>): T{
        return retrofitBUilder.create(serviceType)
    }
}