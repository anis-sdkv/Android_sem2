package com.example.homeworks.presentation.fragments.main

import android.annotation.SuppressLint
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.homeworks.di.ViewModelArgKeys
import com.example.homeworks.domain.entity.WeatherEntity
import com.example.homeworks.domain.usecase.GetWeatherByCityUseCase
import com.example.homeworks.domain.usecase.GetWeatherByCoordsUseCase
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import kotlinx.coroutines.launch

class MainFragmentViewModel(
    private val getCityByNameUseCase: GetWeatherByCityUseCase,
    private val getWeatherByCoordsUseCase: GetWeatherByCoordsUseCase,
    private val locationClient: FusedLocationProviderClient
) : ViewModel() {

    private val _infoBarState = MutableLiveData(InfoBarState.MESSAGE)
    val infoBarState: LiveData<InfoBarState> = _infoBarState

    private val _hintMessageState = MutableLiveData(MessageViewState.START_HINT)
    val hintMessageState: LiveData<MessageViewState> = _hintMessageState

    private val _weatherDataState = MutableLiveData<WeatherEntity?>(null)
    val weatherDataState: LiveData<WeatherEntity?> = _weatherDataState

    private val _requestState = MutableLiveData(false)
    val requestState: LiveData<Boolean> = _requestState

    fun getByCity(city: String) = viewModelScope.launch {
        _requestState.value = true
        _infoBarState.value = InfoBarState.PROGRESS_BAR
        kotlin.runCatching {
            getCityByNameUseCase(city)
        }.onSuccess {
            _weatherDataState.value = it
            _infoBarState.value = InfoBarState.WEATHER
        }.onFailure {
            _hintMessageState.value = MessageViewState.ERROR
            _infoBarState.value = InfoBarState.MESSAGE
        }
        _requestState.value = false
    }

    @SuppressLint("MissingPermission")
    fun getByCoords() {
        _infoBarState.value = InfoBarState.MESSAGE
        _hintMessageState.value = MessageViewState.RECEIVING
        _requestState.value = true
        locationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, object : CancellationToken() {
            override fun onCanceledRequested(p0: OnTokenCanceledListener) = CancellationTokenSource().token
            override fun isCancellationRequested() = false
        }).addOnSuccessListener {
            if (it == null) {
                _hintMessageState.value = MessageViewState.LOCATION_ERROR
                _requestState.value = false
            } else {
                startGetWeatherByCoords(it)
            }
        }.addOnFailureListener {
            _hintMessageState.value = MessageViewState.ERROR
            _requestState.value = false
        }
    }

    private fun startGetWeatherByCoords(location: Location) = viewModelScope.launch {
        kotlin.runCatching {
            _infoBarState.value = InfoBarState.PROGRESS_BAR
            getWeatherByCoordsUseCase(location.latitude, location.longitude)
        }.onSuccess {
            _weatherDataState.value = it
            _infoBarState.value = InfoBarState.WEATHER
        }.onFailure {
            _infoBarState.value = InfoBarState.MESSAGE
            _hintMessageState.value = MessageViewState.ERROR
        }
        _requestState.value = false
    }

    companion object {
        val factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {

            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val getWeatherByCityNameUseCase =
                    extras[ViewModelArgKeys.getWeatherByCityNameUseCase] ?: throw IllegalArgumentException()
                val getWeatherByCoordsUseCase =
                    extras[ViewModelArgKeys.getWeatherByCoordsUseCase] ?: throw IllegalArgumentException()
                val locationClient = extras[ViewModelArgKeys.locationClient] ?: throw IllegalArgumentException()
                return (MainFragmentViewModel(
                    getWeatherByCityNameUseCase, getWeatherByCoordsUseCase, locationClient
                ) as? T) ?: throw java.lang.IllegalStateException()
            }
        }
    }
}
