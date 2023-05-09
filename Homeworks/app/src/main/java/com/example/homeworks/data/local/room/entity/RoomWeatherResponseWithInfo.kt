package com.example.homeworks.data.local.room.entity

import androidx.room.Embedded
import androidx.room.Relation

data class RoomWeatherResponseWithInfo(
    @Embedded val response: RoomWeatherResponse?,
    @Relation(
        parentColumn = "id",
        entityColumn = "responseId"
    )
    val weather: List<RoomWeatherInfo?> = listOf(),
)