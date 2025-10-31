package com.example.countriesweatherapp.controller

import com.example.countriesweatherapp.api.RetrofitClient
import com.example.countriesweatherapp.model.WeatherResponse
import com.example.countriesweatherapp.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherController {

    fun getCurrentWeather(
        city: String,
        onSuccess: (WeatherResponse) -> Unit,
        onError: (String) -> Unit
    ) {
        RetrofitClient.weatherApi.getCurrentWeather(Constants.WEATHER_API_KEY, city)
            .enqueue(object : Callback<WeatherResponse> {
                override fun onResponse(
                    call: Call<WeatherResponse>,
                    response: Response<WeatherResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        onSuccess(response.body()!!)
                    } else {
                        when (response.code()) {
                            401 -> onError("API Key inválida")
                            404 -> onError("Ciudad no encontrada")
                            else -> onError("Error al obtener clima: ${response.code()}")
                        }
                    }
                }

                override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                    onError("Error de conexión al servicio de clima: ${t.message}")
                }
            })
    }
}