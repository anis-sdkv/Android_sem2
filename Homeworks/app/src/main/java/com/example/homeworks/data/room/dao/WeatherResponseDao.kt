package com.example.homeworks.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.homeworks.data.room.entity.RoomWeatherResponse
import com.example.homeworks.data.room.entity.RoomWeatherResponseWithInfo

@Dao
interface WeatherResponseDao {
    @Insert
    fun save(response: RoomWeatherResponse): Long

    @Query("select * from weather_responses where city = :city and createdTime >= :minTimeMs order by createdTime desc limit 1")
    fun getRecentByCity(city: String, minTimeMs: Long): RoomWeatherResponseWithInfo?

    @Query("select * from weather_responses")
    fun getAll(): List<RoomWeatherResponseWithInfo>
}