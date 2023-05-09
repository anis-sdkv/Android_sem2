package com.example.homeworks.data.mapping

import com.example.homeworks.data.local.room.entity.RoomWeatherCoords
import com.example.homeworks.data.local.room.entity.RoomWeatherInfo
import com.example.homeworks.data.local.room.entity.RoomWeatherMainInfo
import com.example.homeworks.data.local.room.entity.RoomWeatherResponse
import com.example.homeworks.data.local.room.entity.RoomWeatherResponseWithInfo
import com.example.homeworks.data.local.room.entity.RoomWindInfo
import com.example.homeworks.data.remote.weather_api.model.response.WeatherResponse
import com.example.homeworks.domain.entity.WeatherCoords
import com.example.homeworks.domain.entity.WeatherEntity
import com.example.homeworks.domain.entity.WeatherInfo
import com.example.homeworks.domain.entity.WeatherMainInfo

fun WeatherResponse.toEntity(): WeatherEntity =
    WeatherEntity(
        coords = WeatherCoords(
            this.coords?.lat,
            this.coords?.lon
        ),
        main = WeatherMainInfo(
            this.main?.temp,
            this.main?.feelsLike,
            this.main?.tempMin,
            this.main?.tempMax,
            this.main?.pressure,
            this.main?.humidity,
        ),
        info = this.weatherList.map {
            WeatherInfo(
                it?.id,
                it?.main,
                it?.description,
                it?.icon,
            )
        },
        cityName = this.cityName,
        windSpeed = this.windInfo?.speed
    )

fun RoomWeatherResponseWithInfo.toEntity(): WeatherEntity {
    val response = this.response
    val weather = this.weather
    return WeatherEntity(
        coords = WeatherCoords(
            response?.coords?.lat,
            response?.coords?.lon
        ),
        main = WeatherMainInfo(
            response?.main?.temp,
            response?.main?.feelsLike,
            response?.main?.tempMin,
            response?.main?.tempMax,
            response?.main?.pressure,
            response?.main?.humidity,
        ),
        info = weather.map {
            WeatherInfo(
                it?.id,
                it?.main,
                it?.description,
                it?.icon,
            )
        },
        cityName = response?.city,
        windSpeed = response?.wind?.speed
    )
}

fun WeatherResponse.toRoomResponse() =
    RoomWeatherResponse(
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

fun WeatherResponse.toRoomInfo(weatherId: Long) =
    this.weatherList.map {
        RoomWeatherInfo(
            it?.id,
            it?.main,
            it?.description,
            it?.icon,
            weatherId
        )
    }
