package com.example.homeworks.presentation.tools

import com.example.homeworks.data.api.model.response.*
import com.example.homeworks.data.room.entity.*

fun RoomWeatherResponseWithInfo.toApiResponse(): WeatherResponse {
    return WeatherResponse(
        WeatherCoords(
            this.response?.coord?.lat,
            this.response?.coord?.lon
        ),
        this.weather?.map { WeatherInfo(it.id, it.main, it.description, it.icon) },
        WeatherMainInfo(
            this.response?.main?.temp,
            this.response?.main?.feelsLike,
            this.response?.main?.tempMin,
            this.response?.main?.tempMax,
            this.response?.main?.pressure,
            this.response?.main?.humidity,
        ),
        this.response?.city,
        WindInfo(this.response?.wind?.speed)
    )
}

fun WeatherResponse.toRoomResponse(): RoomWeatherResponse {
    return RoomWeatherResponse(
        this.cityName ?: "undefined",
        RoomWeatherCoords(this.coords?.lat, this.coords?.lon),
        RoomWeatherMainInfo(
            this.main?.temp,
            this.main?.feelsLike,
            this.main?.tempMin,
            this.main?.tempMax,
            this.main?.pressure,
            this.main?.humidity,
        ),
        RoomWindInfo(this.windInfo?.speed),
    )
}
