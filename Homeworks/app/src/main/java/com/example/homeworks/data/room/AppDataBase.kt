package com.example.homeworks.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.homeworks.data.room.dao.WeatherResponseDao
import com.example.homeworks.data.room.entity.RoomWeatherInfo
import com.example.homeworks.data.room.entity.RoomWeatherResponse

@Database(entities = [RoomWeatherResponse::class, RoomWeatherInfo::class], version = 1)
abstract class AppDataBase : RoomDatabase() {
    abstract fun getWeatherResponseDao(): WeatherResponseDao

    companion object {
        private const val DATABASE_NAME = "homeworks.db"

        private var INSTANCE: AppDataBase? = null

        fun getInstance(context: Context): AppDataBase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE =
                        Room.databaseBuilder(context,AppDataBase::class.java, DATABASE_NAME)
                            .build()
                }
            }
            return INSTANCE!!
        }
    }
}