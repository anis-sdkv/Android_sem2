package com.example.homeworks.presentation.fragments.bottom

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.homeworks.R
import com.example.homeworks.data.remote.weather_api.model.response.WeatherResponse
import com.example.homeworks.databinding.FragmentWeatherBottomSheetBinding
import com.example.homeworks.domain.entity.WeatherEntity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class WeatherBottomSheetFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentWeatherBottomSheetBinding? = null
    private val binding get() = _binding!!

    override fun getTheme() = R.style.AppBottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherBottomSheetBinding.bind(
            inflater.inflate(
                R.layout.fragment_weather_bottom__sheet,
                container
            )
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initWeatherInfo()
    }

    private fun initWeatherInfo() {
        val weatherInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("WEATHER_INFO_KEY", WeatherEntity::class.java)
        } else {
            arguments?.getParcelable("WEATHER_INFO_KEY")
        }
        with(binding) {
            tvCityValue.text = weatherInfo?.cityName?.replaceFirstChar {
                it.uppercaseChar()
            }
            tvTempValue.text = weatherInfo?.main?.temp?.toString()
            tvHumidityValue.text = weatherInfo?.main?.humidity?.toString()
            tvPressureValue.text = weatherInfo?.main?.pressure?.toString()
            tvWindSpeedValue.text = weatherInfo?.windSpeed?.toString()
            val info = weatherInfo?.info?.firstOrNull()
            tvDescriptionValue.text =
                info?.description?.replaceFirstChar {
                    it.uppercaseChar()
                }
            info?.icon?.let { loadImage(ivWeatherIcon, it) }
        }
    }

    private fun loadImage(view: ImageView, icon: String) {
        Glide.with(requireContext())
            .load("https://openweathermap.org/img/wn/${icon}@4x.png")
            .placeholder(R.drawable.ic_image_not_found)
            .error(R.drawable.ic_image_not_found)
            .centerCrop()
            .into(view)
    }

    companion object {
        const val WEATHER_INFO_KEY = "WEATHER_INFO_KEY"
        const val FRAGMENT_TAG = "WEATHER_BOTTOM_SHEET_FRAGMENT"
        fun getInstance() = WeatherBottomSheetFragment()
    }
}