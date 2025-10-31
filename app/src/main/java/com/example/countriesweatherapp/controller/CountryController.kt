package com.example.countriesweatherapp.controller

import com.example.countriesweatherapp.api.RetrofitClient
import com.example.countriesweatherapp.model.Country
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CountryController {

    fun getAllCountries(
        onSuccess: (List<Country>) -> Unit,
        onError: (String) -> Unit
    ) {
        RetrofitClient.countriesApi.getAllCountries().enqueue(object : Callback<List<Country>> {
            override fun onResponse(call: Call<List<Country>>, response: Response<List<Country>>) {
                if (response.isSuccessful && response.body() != null) {
                    onSuccess(response.body()!!)
                } else {
                    onError("Error al obtener países: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Country>>, t: Throwable) {
                onError("Error de conexión: ${t.message}")
            }
        })
    }

    fun getCountriesByRegion(
        region: String,
        onSuccess: (List<Country>) -> Unit,
        onError: (String) -> Unit
    ) {
        RetrofitClient.countriesApi.getCountriesByRegion(region)
            .enqueue(object : Callback<List<Country>> {
                override fun onResponse(call: Call<List<Country>>, response: Response<List<Country>>) {
                    if (response.isSuccessful && response.body() != null) {
                        onSuccess(response.body()!!)
                    } else {
                        onError("Error al obtener países de la región: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<List<Country>>, t: Throwable) {
                    onError("Error de conexión: ${t.message}")
                }
            })
    }
}