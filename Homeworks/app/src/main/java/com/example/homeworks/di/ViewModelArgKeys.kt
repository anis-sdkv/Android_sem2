package com.example.homeworks.di

import androidx.lifecycle.viewmodel.CreationExtras
import com.example.homeworks.domain.usecase.GetWeatherByCityUseCase
import com.example.homeworks.domain.usecase.GetWeatherByCoordsUseCase
import com.google.android.gms.location.FusedLocationProviderClient

object ViewModelArgKeys {
    val getWeatherByCityNameUseCase = object : CreationExtras.Key<GetWeatherByCityUseCase> {}
    val getWeatherByCoordsUseCase = object : CreationExtras.Key<GetWeatherByCoordsUseCase> {}
    val locationClient = object : CreationExtras.Key<FusedLocationProviderClient> {}
}