package com.example.homeworks.di

import android.content.Context
import com.example.homeworks.data.local.room.AppDataBase
import com.example.homeworks.data.local.room.RoomWeatherDataSource
import com.example.homeworks.data.remote.weather_api.OpenWeatherService
import com.example.homeworks.data.repository.WeatherRepositoryImpl
import com.example.homeworks.domain.repository.WeatherRepository
import com.example.homeworks.domain.usecase.GetWeatherByCityUseCase
import com.example.homeworks.domain.usecase.GetWeatherByCoordsUseCase
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

object DataDependency {
    private lateinit var localSource: RoomWeatherDataSource
    private lateinit var locationClient: FusedLocationProviderClient
    private var remoteSource = OpenWeatherService.getInstance()
    private lateinit var weatherRepository: WeatherRepository

    private var init = false

    fun init(context: Context) {
        if (init) return
        locationClient = LocationServices.getFusedLocationProviderClient(context)
        localSource = RoomWeatherDataSource(AppDataBase.getInstance(context))
        weatherRepository = WeatherRepositoryImpl(localSource, remoteSource)
        init = true
    }

    fun getWeatherByCityUseCase() = GetWeatherByCityUseCase(weatherRepository)
    fun getWeatherByCoordsUseCase() = GetWeatherByCoordsUseCase(weatherRepository)
    fun getLocationClient() = locationClient
}