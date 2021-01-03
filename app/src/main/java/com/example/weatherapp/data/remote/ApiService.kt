package com.example.weatherapp.data.remote

import com.example.weatherapp.data.remote.response.weather.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("data/2.5/weather?")
    suspend fun getWeatherByCityName(
        @Query("q") keyword: String,
        @Query("APPID") app_id: String,
        @Query("units") units: String
    ): Response<WeatherResponse>

    @GET("data/2.5/weather?")
    suspend fun getWeatherByZipCode(
        @Query("zip") zipCode: String,
        @Query("APPID") app_id: String,
        @Query("units") units: String
    ): Response<WeatherResponse>

    @GET("data/2.5/weather?")
    suspend fun getWeatherByGPS(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("APPID") app_id: String,
        @Query("units") units: String
    ): Response<WeatherResponse>
}