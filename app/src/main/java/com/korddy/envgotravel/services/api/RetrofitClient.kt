package com.korddy.envgotravel.services.api

import com.korddy.envgotravel.services.session.SessionCache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "https://envgotravelbase.onrender.com/api/v1/"

    private val authInterceptor = Interceptor { chain ->
        val original: Request = chain.request()
        val builder = original.newBuilder()

        // Token do cache (recarregado a cada req)
        SessionCache.getAuthToken()?.let { token ->
            builder.addHeader("Authorization", "Bearer $token")
        }

        builder.addHeader("Accept", "application/json")
        builder.addHeader("Content-Type", "application/json")

        chain.proceed(builder.build())
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .build()

    val api: ApiService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)
}