package com.example.homeworks.data.room.entity

import androidx.room.*

@Entity(tableName = "weather_responses")
data class RoomWeatherResponse(
    val city: String,
    @Embedded
    val coord: RoomWeatherCoords? = null,
    @Embedded
    val main: RoomWeatherMainInfo? = null,
    @Embedded
    val wind: RoomWindInfo? = null,
    val createdTime: Long = System.currentTimeMillis(),
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
)

data class RoomWeatherCoords(
    val lat: Float? = null,
    val lon: Float? = null
)

data class RoomWeatherMainInfo(
    val temp: Float? = null,
    val feelsLike: Float? = null,
    val tempMin: Float? = null,
    val tempMax: Float? = null,
    val pressure: Float? = null,
    val humidity: Float? = null
)

data class RoomWindInfo(
    val speed: Float? = null
)