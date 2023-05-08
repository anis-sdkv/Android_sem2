package com.example.homeworks.data.room

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.homeworks.data.room.entity.RoomWeatherResponse
import com.example.homeworks.data.room.entity.RoomWeatherResponseWithInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Duration

class RoomWeatherRepository(context: Context) {

    private val db = AppDataBase.getInstance(context)

    private val weatherResponseDao by lazy {
        db.getWeatherResponseDao()
    }

    suspend fun saveWeatherResponse(response: RoomWeatherResponse): Long =
        withContext(Dispatchers.IO) {
            weatherResponseDao.save(response)
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