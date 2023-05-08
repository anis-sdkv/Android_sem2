package com.example.homeworks.data.api.model

import com.example.homeworks.data.api.model.response.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherApiService {

    @GET("weather")
    suspend fun getWeatherByCityName(
        @Query("q") city: String
    ): WeatherResponse?

    @GET("weather")
    suspend fun getWeatherByCoords(
        @Query("lon") log: Double,
        @Query("lat") lat: Double
    ): WeatherResponse?
}