package com.example.homeworks.domain.repository

import com.example.homeworks.domain.entity.WeatherEntity

interface WeatherRepository {
    suspend fun getWeatherInfoByCityName(city: String): WeatherEntity?
    suspend fun getWeatherInfoByCoords(lon: Double, lat: Double): WeatherEntity?
}