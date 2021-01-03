package com.example.weatherapp.di

import android.content.Context
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.data.local.AppDatabase
import com.example.weatherapp.data.local.RecentSearchesDao
import com.example.weatherapp.data.remote.WeatherRemoteDataSource
import com.example.weatherapp.data.remote.ApiService
import com.example.weatherapp.data.repository.RecentSearchesRepository
import com.example.weatherapp.data.repository.WeatherRepository
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson): Retrofit = Retrofit.Builder()
        .baseUrl("http://api.openweathermap.org/")
        .client(
            OkHttpClient().newBuilder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = if (BuildConfig.DEBUG) Level.BODY else Level.NONE
                })
                .build()
        )
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()


    @Provides
    fun provideWeatherService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

    @Singleton
    @Provides
    fun provideWeatherRemoteDataSource(service: ApiService) =
        WeatherRemoteDataSource(service)

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) =
        AppDatabase.getDatabase(appContext)

    @Singleton
    @Provides
    fun provideWeatherDao(db: AppDatabase) = db.recentSearchesDao()

    @Singleton
    @Provides
    fun provideWeatherRepository(
        remoteDataSource: WeatherRemoteDataSource
    ) = WeatherRepository(remoteDataSource)

    @Singleton
    @Provides
    fun provideRecentSearchesRepository(
        localDataSource: RecentSearchesDao
    ) = RecentSearchesRepository(localDataSource)
}