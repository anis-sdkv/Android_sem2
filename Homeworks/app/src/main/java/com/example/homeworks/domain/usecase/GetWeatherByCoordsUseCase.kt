package com.example.homeworks.domain.usecase

import com.example.homeworks.domain.repository.WeatherRepository

class GetWeatherByCoordsUseCase(private val weatherRepository: WeatherRepository) {
    suspend operator fun invoke(lat: Double, lon: Double) =
        weatherRepository.getWeatherInfoByCoords(lat, lon)
}