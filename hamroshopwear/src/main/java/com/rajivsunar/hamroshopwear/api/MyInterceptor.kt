package com.rajivsunar.hamroshopwear.api

import okhttp3.Interceptor
import okhttp3.Response

class MyInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader("Authorization", "${ServiceBuilder.token}")
            .build()
        return chain.proceed(request)
    }
}