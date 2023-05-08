package com.example.homeworks.data.api.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class WeatherResponse(
    @SerializedName("coord")
    val coords: WeatherCoords? = null,
    @SerializedName("weather")
    val weatherList: List<WeatherInfo>? = null,
    @SerializedName("main")
    val main: WeatherMainInfo? = null,
    @SerializedName("name")
    val cityName: String? = null,
    @SerializedName("wind")
    val windInfo: WindInfo? = null
) : Parcelable

@Parcelize
data class WindInfo(
    @SerializedName("speed")
    val speed: Float? = null
) : Parcelable

@Parcelize
data class WeatherCoords(
    @SerializedName("lat")
    val lat: Float? = null,
    @SerializedName("lon")
    val lon: Float? = null
) : Parcelable

@Parcelize
data class WeatherInfo(
    @SerializedName("id")
    val id: Long? = null,
    @SerializedName("main")
    val main: String? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("icon")
    val icon: String? = null
) : Parcelable

@Parcelize
data class WeatherMainInfo(
    @SerializedName("temp")
    val temp: Float? = null,
    @SerializedName("feels_like")
    val feelsLike: Float? = null,
    @SerializedName("temp_min")
    val tempMin: Float? = null,
    @SerializedName("temp_max")
    val tempMax: Float? = null,
    @SerializedName("pressure")
    val pressure: Float? = null,
    @SerializedName("humidity")
    val humidity: Float? = null
) : Parcelable