package com.example.homeworks.data.local.room

import com.example.homeworks.data.local.room.entity.RoomWeatherResponseWithInfo
import com.example.homeworks.data.mapping.toRoomInfo
import com.example.homeworks.data.mapping.toRoomResponse
import com.example.homeworks.data.remote.weather_api.model.response.WeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Duration

class RoomWeatherDataSource(db: AppDataBase) {
    private val weatherResponseDao by lazy {
        db.getWeatherResponseDao()
    }

    suspend fun saveWeatherResponse(response: WeatherResponse) =
        withContext(Dispatchers.IO) {
            val id = weatherResponseDao.saveResponse(response.toRoomResponse())
            weatherResponseDao.saveInfos(response.toRoomInfo(id))
        }

    suspend fun getWeatherResponse(city: String): RoomWeatherResponseWithInfo? =
        withContext(Dispatchers.IO) {
            weatherResponseDao.getRecentByCity(
                city,
                System.currentTimeMillis() - Duration.ofMinutes(1).toMillis()
            )
        }

    suspend fun getAllWeatherResponse(): List<RoomWeatherResponseWithInfo> =
        withContext(Dispatchers.IO) {
            weatherResponseDao.getAll()
        }
}