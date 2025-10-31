package com.example.countriesweatherapp.model


import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("current")
    val current: CurrentWeather
)

data class CurrentWeather(
    @SerializedName("temp_c")
    val tempC: Double,

    @SerializedName("temp_f")
    val tempF: Double,

    @SerializedName("condition")
    val condition: WeatherCondition,

    @SerializedName("wind_kph")
    val windKph: Double,

    @SerializedName("wind_mph")
    val windMph: Double,

    @SerializedName("humidity")
    val humidity: Int
)

data class WeatherCondition(
    @SerializedName("text")
    val text: String,

    @SerializedName("icon")
    val icon: String
)