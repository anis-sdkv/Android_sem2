package com.example.homeworks.data.api.reporsitory

import com.example.homeworks.data.api.OpenWeatherService
import com.example.homeworks.data.api.model.response.WeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WeatherRepository {
    suspend fun getWeatherInfoByCityName(city: String): WeatherResponse? {
        return withContext(Dispatchers.IO) {
            OpenWeatherService.getInstance().getWeatherByCityName(city)
        }
    }

    suspend fun getWetherInfoByCoords(lon: Double, lat: Double): WeatherResponse? {
        return withContext(Dispatchers.IO){
            OpenWeatherService.getInstance().getWeatherByCoords(lon, lat)
        }
    }
}