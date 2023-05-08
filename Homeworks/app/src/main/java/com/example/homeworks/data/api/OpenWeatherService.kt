package com.example.homeworks.data.api

import com.example.homeworks.BuildConfig
import com.example.homeworks.data.api.model.OpenWeatherApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object OpenWeatherService {
    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val requestUrl = chain.request().url
                val newUrl = requestUrl.newBuilder()
                    .addQueryParameter("appid", BuildConfig.WEATHER_KEY)
                    .addQueryParameter("units", "metric")
                    .build()
                chain.proceed(chain.request().newBuilder().url(newUrl).build())
            }
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    setLevel(HttpLoggingInterceptor.Level.BODY)
                }
            )
            .build()
    }

    private val retrofitInstance: OpenWeatherApiService by lazy {
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl(BuildConfig.WEATHER_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofitBuilder.create<OpenWeatherApiService>()
    }

    fun getInstance(): OpenWeatherApiService = retrofitInstance
}
