package com.example.homeworks.data.local.room.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.homeworks.domain.entity.WeatherInfo

@Entity(
    tableName = "weather_info",
    foreignKeys = [ForeignKey(
        entity = RoomWeatherResponse::class,
        parentColumns = ["id"],
        childColumns = ["responseId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class RoomWeatherInfo(
    val infoId: Long? = null,
    val main: String? = null,
    val description: String? = null,
    val icon: String? = null,
    val responseId: Long,
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
)