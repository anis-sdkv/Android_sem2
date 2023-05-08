package com.example.homeworks.presentation.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.homeworks.R
import com.example.homeworks.data.api.model.response.WeatherResponse
import com.example.homeworks.data.api.reporsitory.WeatherRepository
import com.example.homeworks.databinding.FragmentMainBinding
import com.example.homeworks.presentation.MainActivity
import com.example.homeworks.presentation.tools.toApiResponse
import com.example.homeworks.presentation.tools.toRoomResponse
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException


class MainFragment : Fragment(R.layout.fragment_main) {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val activity get() = requireActivity() as MainActivity

    private var _repository: WeatherRepository? = null
    private val repository get() = _repository!!

    private var _fusedLocationClient: FusedLocationProviderClient? = null
    private val fusedLocationClient get() = _fusedLocationClient!!

    private var currentWeatherInfo: WeatherResponse? = null
    private var requestStarted = false
    private var hintMessage: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hintMessage = getString(R.string.start_hint)
        _binding = FragmentMainBinding.bind(view)
        _repository = WeatherRepository()
        _fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        initClickListeners()
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

    private fun handleRequestBtn() {
        with(binding) {
            if (etCity.text.isEmpty()) {
                Toast.makeText(requireContext(), EMPTY_INPUT, Toast.LENGTH_SHORT).show()
                return
            }
            if (!requestStarted) {
                startRequest {
                    val success = tryGetFromCache(etCity.text.toString())
                    if (!success) getByCity(etCity.text.toString())
                }
            }
        }
    }

    private suspend fun tryGetFromCache(city: String): Boolean {
        val response = MainActivity.repository?.getWeatherResponse(city) ?: return false
        Toast.makeText(requireContext(), "Получено из кеша", Toast.LENGTH_SHORT)
            .show()
        currentWeatherInfo = response.toApiResponse()
        return true
    }

    private fun handleRequestWithCoordsBtn() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionWithRationale()
            return
        }

        requestStarted = true
        hideAllFromResultView()
        with(binding) {
            tvHint.visibility = View.VISIBLE
            tvHint.text = "Получение геолокации..."
        }
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, object : CancellationToken() {
            override fun onCanceledRequested(p0: OnTokenCanceledListener) = CancellationTokenSource().token
            override fun isCancellationRequested() = false
        })
            .addOnCompleteListener {
                requestStarted = false
                hideAllFromResultView()
                binding.tvHint.text = hintMessage
            }
            .addOnSuccessListener {

                if (it == null) {
                    showLocationErrorToast()
                } else {
                    startRequest { getByCoords(it) }
                }
            }.addOnFailureListener {
                showLocationErrorToast()
            }
    }

    private fun hideAllFromResultView() {
        with(binding) {
            tvHint.visibility = View.INVISIBLE
            tvCity.visibility = View.INVISIBLE
            tvTemp.visibility = View.INVISIBLE
            tvTempSymbol.visibility = View.INVISIBLE
            tvClickHint.visibility = View.INVISIBLE
        }
    }

    private fun updateResultView() {
        with(binding) {
            if (currentWeatherInfo == null) {
                tvHint.visibility = View.VISIBLE
                tvHint.text = hintMessage
            } else {
                tvCity.visibility = View.VISIBLE
                tvTemp.visibility = View.VISIBLE
                tvClickHint.visibility = View.VISIBLE
                tvTempSymbol.visibility = View.VISIBLE

                tvCity.text = currentWeatherInfo?.cityName
                tvTemp.text = currentWeatherInfo?.main?.temp?.toString()
            }
        }
    }

    private fun showBottomSheet() {
        val fragment = WeatherBottomSheetFragment.getInstance()
        fragment.arguments = bundleOf(WeatherBottomSheetFragment.WEATHER_INFO_KEY to currentWeatherInfo)
        fragment.show(
            requireActivity().supportFragmentManager, WeatherBottomSheetFragment.FRAGMENT_TAG
        )
    }

    private fun startRequest(request: suspend () -> Unit) {
        requestStarted = true
        hideAllFromResultView()
        binding.pbRequest.visibility = View.VISIBLE
        lifecycleScope.launch {
            request()
            withContext(Dispatchers.Main) {
                binding.pbRequest.visibility = View.GONE
                updateResultView()
                requestStarted = false
            }
        }
    }

    private suspend fun getByCity(city: String) = withContext(Dispatchers.IO) {
        kotlin.runCatching {
            repository.getWeatherInfoByCityName(city)
        }.onFailure {
            currentWeatherInfo = null
            hintMessage = if (it is HttpException && it.code() == 404) {
                CITY_NOT_FOUND
            } else {
                "Не удалось получить ответ: ${it.message}"
            }
        }.onSuccess {
            currentWeatherInfo = it
            it?.toRoomResponse()?.let { it1 ->
                MainActivity.repository?.saveWeatherResponse(it1)
            }
        }
    }

    private suspend fun getByCoords(location: Location) {
        kotlin.runCatching {
            repository.getWetherInfoByCoords(location.longitude, location.latitude)
        }.onFailure {
            currentWeatherInfo = null
            hintMessage = "Не удалось получить ответ: ${it.message}"
        }.onSuccess {
            currentWeatherInfo = it
        }
    }

    private fun requestPermissionWithRationale() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            Snackbar.make(
                requireActivity().findViewById(android.R.id.content),
                "Allow access to location",
                Snackbar.LENGTH_INDEFINITE
            ).setAction(
                "Allow".uppercase()
            ) { requestPerms() }.show()
        } else {
            requestPerms()
        }
    }

    private fun requestPerms() {
        activity.requestPermissions.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private fun showLocationErrorToast() {
        Toast.makeText(
            requireContext(),
            LOCATION_ERROR,
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _repository = null
        currentWeatherInfo = null
        hintMessage = null
    }

    companion object {
        private const val CITY_NOT_FOUND = "Город не найден"
        private const val EMPTY_INPUT = "Название города не может быть пустым"
        private const val LOCATION_ERROR =
            "Не удалось получить текущее местоположение, попробуйте включить геолокацию в настройках"

        const val FRAGMENT_TAG = "MAIN_FRAGMENT"
        fun getInstance() = MainFragment()
    }
}
