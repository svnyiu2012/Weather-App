package com.example.weatherapp.data.remote

import javax.inject.Inject

const val API_KEY = "95d190a434083879a6398aafd54d9e73"

class WeatherRemoteDataSource @Inject constructor(
    private val apiService: ApiService
) : BaseDataSource() {

    suspend fun getWeatherByCityName(keyword: String) =
        getResult { apiService.getWeatherByCityName(keyword, API_KEY, "metric") }

    suspend fun getWeatherByZipCode(zipCode: String) =
        getResult { apiService.getWeatherByZipCode(zipCode, API_KEY, "metric") }

    suspend fun getWeatherByGPS(lat: Double, lon: Double) =
        getResult { apiService.getWeatherByGPS(lat, lon, API_KEY, "metric") }
}