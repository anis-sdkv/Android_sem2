package com.example.homeworks.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.homeworks.data.local.room.entity.RoomWeatherInfo
import com.example.homeworks.data.local.room.entity.RoomWeatherResponse
import com.example.homeworks.data.local.room.entity.RoomWeatherResponseWithInfo

@Dao
interface WeatherResponseDao {
    @Insert
    fun saveResponse(response: RoomWeatherResponse): Long

    @Insert
    fun saveInfos(infos: List<RoomWeatherInfo>) : List<Long>

    @Query("select * from weather_responses where city = :city and createdTime >= :minTimeMs order by createdTime desc limit 1")
    fun getRecentByCity(city: String, minTimeMs: Long): RoomWeatherResponseWithInfo?

    @Query("select * from weather_responses")
    fun getAll(): List<RoomWeatherResponseWithInfo>
}