package com.example.homeworks.data.remote.weather_api

import com.example.homeworks.data.remote.weather_api.model.response.WeatherResponse
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