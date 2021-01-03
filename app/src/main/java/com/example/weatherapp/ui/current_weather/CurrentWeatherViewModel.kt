package com.example.weatherapp.ui.current_weather

import android.annotation.SuppressLint
import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weatherapp.data.local.entity.RecentSearchesEntry
import com.example.weatherapp.data.remote.response.weather.WeatherResponse
import com.example.weatherapp.data.repository.RecentSearchesRepository
import com.example.weatherapp.data.repository.WeatherRepository
import com.example.weatherapp.utils.Coroutines
import com.example.weatherapp.data.remote.utils.Resource
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import java.util.*

class CurrentWeatherViewModel @ViewModelInject constructor(
    application: Application,
    private val weatherRepository: WeatherRepository,
    private val recentSearchesRepository: RecentSearchesRepository,
) : AndroidViewModel(application) {

    private var fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)

    private val _weather = MutableLiveData<Resource<WeatherResponse>>()
    val weather: LiveData<Resource<WeatherResponse>> = _weather

    //get most recent searches in db
    fun getMostRecentSearchesWeather() {
        Coroutines.io {
            if (recentSearchesRepository.getMostRecentSearches() != null) {
                _weather.postValue(weatherRepository.getWeatherByCityName(recentSearchesRepository.getMostRecentSearches().name))
            }
        }
    }

    //add or update recent searches list in db
    fun updateRecentSearches(id: String, name: String) {
        val entry = RecentSearchesEntry(id, name, Calendar.getInstance().timeInMillis)
        Coroutines.io {
            recentSearchesRepository.updateRecentSearches(entry)
        }
    }

    //get weather by city name
    fun getWeatherByCityName(keyword: String) {
        Coroutines.io {
            _weather.postValue(weatherRepository.getWeatherByCityName(keyword))
        }
    }

    //get weather by zipCode
    fun getWeatherByZipCode(keyword: String) {
        Coroutines.io {
            _weather.postValue(weatherRepository.getWeatherByZipCode(keyword))
        }
    }

    //get weather by gps
    @SuppressLint("MissingPermission")
    fun getWeatherByGPS() {
        val locationRequest: LocationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null
        )
    }

    // gps location callback
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            fusedLocationClient.removeLocationUpdates(this)
            locationResult ?: return
            Coroutines.io {
                _weather.postValue(
                    weatherRepository.getWeatherByGPS(
                        locationResult.locations[0].latitude,
                        locationResult.locations[0].longitude
                    )
                )
            }
        }
    }
}