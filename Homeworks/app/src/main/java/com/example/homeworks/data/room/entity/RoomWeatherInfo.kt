package com.example.homeworks.data.room.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

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
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val responseId: Long,
    val main: String? = null,
    val description: String? = null,
    val icon: String? = null
)