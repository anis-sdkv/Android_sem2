package com.example.homeworks.data.repository

import com.example.homeworks.data.local.room.RoomWeatherDataSource
import com.example.homeworks.data.mapping.toEntity
import com.example.homeworks.data.mapping.toRoomResponse
import com.example.homeworks.data.remote.weather_api.OpenWeatherApiService
import com.example.homeworks.domain.entity.WeatherEntity
import com.example.homeworks.domain.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WeatherRepositoryImpl(
    private val localDataSource: RoomWeatherDataSource,
    private val remoteDataSource: OpenWeatherApiService
) : WeatherRepository {

    override suspend fun getWeatherInfoByCityName(city: String): WeatherEntity? =
        withContext(Dispatchers.IO) {
            val local = localDataSource
                .getWeatherResponse(city)
                ?.toEntity()
            if (local != null) return@withContext local
            val remote = remoteDataSource
                .getWeatherByCityName(city) ?: return@withContext null
            launch {
                localDataSource.saveWeatherResponse(remote)
            }
            return@withContext remote.toEntity()
        }

    override suspend fun getWeatherInfoByCoords(lon: Double, lat: Double): WeatherEntity? =
        withContext(Dispatchers.IO) {
            remoteDataSource
                .getWeatherByCoords(lon, lat)
                ?.toEntity()
        }
}