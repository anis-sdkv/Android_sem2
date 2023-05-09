package com.example.homeworks.domain.usecase

import com.example.homeworks.domain.repository.WeatherRepository

class GetWeatherByCityUseCase(private val weatherRepository: WeatherRepository) {
    suspend operator fun invoke(city: String) =
        weatherRepository.getWeatherInfoByCityName(city)
}