package com.mintusharma.movieapp

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private const val BASE_URL = "http://www.omdbapi.com/"
    private var instance: RetrofitClient? = null
    private lateinit var mApi: ApiServices

    private fun createRetrofitInstance(): Retrofit {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Synchronized
    fun getInstance(): RetrofitClient {
        if (instance == null) {
            instance = RetrofitClient()
        }
        return instance!!
    }

    fun getApi(): ApiServices {
        if (!::mApi.isInitialized) {
            val retrofit = createRetrofitInstance()
            mApi = retrofit.create(ApiServices::class.java)
        }
        return mApi
    }
}