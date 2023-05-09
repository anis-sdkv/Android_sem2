package com.example.homeworks.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WeatherEntity(
    val coords: WeatherCoords? = null,
    val main: WeatherMainInfo? = null,
    val info: List<WeatherInfo?> = listOf(),
    val cityName: String? = null,
    val windSpeed: Float? = null
) : Parcelable

@Parcelize
data class WeatherCoords(
    val lat: Float? = null,
    val lon: Float? = null
) : Parcelable

@Parcelize
data class WeatherInfo(
    val id: Long? = null,
    val main: String? = null,
    val description: String? = null,
    val icon: String? = null
) : Parcelable

@Parcelize
data class WeatherMainInfo(
    val temp: Float? = null,
    val feelsLike: Float? = null,
    val tempMin: Float? = null,
    val tempMax: Float? = null,
    val pressure: Float? = null,
    val humidity: Float? = null
) : Parcelable
