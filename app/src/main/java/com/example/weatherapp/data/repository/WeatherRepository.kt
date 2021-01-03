package com.example.weatherapp.data.repository

import com.example.weatherapp.data.remote.BaseDataSource
import com.example.weatherapp.data.remote.WeatherRemoteDataSource
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val remoteDataSource: WeatherRemoteDataSource
) : BaseDataSource() {
    suspend fun getWeatherByCityName(keyword: String) = remoteDataSource.getWeatherByCityName(keyword)

    suspend fun getWeatherByZipCode(zipCode: String) = remoteDataSource.getWeatherByZipCode(zipCode)

    suspend fun getWeatherByGPS(lat: Double, lon: Double) =
        remoteDataSource.getWeatherByGPS(lat, lon)
}