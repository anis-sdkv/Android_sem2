package com.example.homeworks.presentation.fragments.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.MutableCreationExtras
import com.example.homeworks.R
import com.example.homeworks.databinding.FragmentMainBinding
import com.example.homeworks.di.DataDependency
import com.example.homeworks.di.ViewModelArgKeys
import com.example.homeworks.domain.entity.WeatherEntity
import com.example.homeworks.presentation.base.BaseActivity
import com.example.homeworks.presentation.fragments.bottom.WeatherBottomSheetFragment


class MainFragment : Fragment(R.layout.fragment_main) {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainFragmentViewModel by viewModels(extrasProducer = {
        MutableCreationExtras().apply {
            set(ViewModelArgKeys.getWeatherByCityNameUseCase, DataDependency.getWeatherByCityUseCase())
            set(ViewModelArgKeys.getWeatherByCoordsUseCase, DataDependency.getWeatherByCoordsUseCase())
            set(ViewModelArgKeys.locationClient, DataDependency.getLocationClient())
        }
    }) {
        MainFragmentViewModel.factory
    }

    private var currentWeatherInfo: WeatherEntity? = null
    private var requestStarted = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMainBinding.bind(view)
        initClickListeners()
        observeData()
    }


    private fun initClickListeners() {
        with(binding) {
            btnRequest.setOnClickListener {
                handleRequestBtn()
            }

            btnRequestCoords.setOnClickListener {
                handleRequestWithCoordsBtn()
            }

            clRequestInfo.setOnClickListener {
                if (currentWeatherInfo != null) showBottomSheet()
            }
        }
    }

    private fun observeData() {
        with(binding) {
            viewModel.infoBarState.observe(viewLifecycleOwner) { state ->
                val weatherViews = listOf(tvCity, tvTemp, tvClickHint, tvTempSymbol)
                weatherViews.forEach { it.visibility = View.INVISIBLE }
                pbRequest.visibility = View.INVISIBLE
                tvHint.visibility = View.INVISIBLE
                when (state) {
                    InfoBarState.WEATHER -> {
                        weatherViews.forEach { it.visibility = View.VISIBLE }
                    }

                    InfoBarState.PROGRESS_BAR -> {
                        pbRequest.visibility = View.VISIBLE
                    }

                    else -> {
                        tvHint.visibility = View.VISIBLE
                    }
                }

            }
            viewModel.hintMessageState.observe(viewLifecycleOwner) { state ->
                when (state) {
                    MessageViewState.START_HINT -> tvHint.text = getString(R.string.start_hint)
                    MessageViewState.RECEIVING -> tvHint.text = getText(R.string.receiving_geo)
                    MessageViewState.ERROR -> tvHint.text = getText(R.string.error_message)
                    MessageViewState.LOCATION_ERROR -> tvHint.text = getText(R.string.location_error)
                    else -> throw IllegalArgumentException()
                }
            }
            viewModel.weatherDataState.observe(viewLifecycleOwner) { weather ->
                currentWeatherInfo = weather
                tvCity.text = currentWeatherInfo?.cityName
                tvTemp.text = currentWeatherInfo?.main?.temp?.toString()
            }
            viewModel.requestState.observe(viewLifecycleOwner) { isStarted ->
                requestStarted = isStarted
            }
        }
    }

    private fun handleRequestBtn() {
        with(binding) {
            if (etCity.text.isEmpty())
                Toast.makeText(requireContext(), getString(R.string.empty_input_error), Toast.LENGTH_SHORT).show()
            else if (!requestStarted)
                viewModel.getByCity(etCity.text.toString())
        }
    }

    private fun handleRequestWithCoordsBtn() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        )
            (requireActivity() as BaseActivity)
                .requestPermissionWithRationale(Manifest.permission.ACCESS_FINE_LOCATION)
        else if (!requestStarted)
            viewModel.getByCoords()
    }

    private fun showBottomSheet() {
        val fragment = WeatherBottomSheetFragment.getInstance()
        fragment.arguments = bundleOf(WeatherBottomSheetFragment.WEATHER_INFO_KEY to currentWeatherInfo)
        fragment.show(
            requireActivity().supportFragmentManager, WeatherBottomSheetFragment.FRAGMENT_TAG
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val FRAGMENT_TAG = "MAIN_FRAGMENT"
        fun getInstance() = MainFragment()
    }
}
