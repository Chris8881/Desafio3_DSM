package com.example.countriesweatherapp.api

import com.example.countriesweatherapp.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val countriesRetrofit = Retrofit.Builder()
        .baseUrl(Constants.COUNTRIES_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val weatherRetrofit = Retrofit.Builder()
        .baseUrl(Constants.WEATHER_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val countriesApi: CountriesApiService = countriesRetrofit.create(CountriesApiService::class.java)
    val weatherApi: WeatherApiService = weatherRetrofit.create(WeatherApiService::class.java)
}