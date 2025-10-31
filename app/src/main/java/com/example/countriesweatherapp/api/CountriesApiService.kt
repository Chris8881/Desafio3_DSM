package com.example.countriesweatherapp.api


import com.example.countriesweatherapp.model.Country
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface CountriesApiService {

    @GET("v3.1/all")
    fun getAllCountries(): Call<List<Country>>

    @GET("v3.1/region/{region}")
    fun getCountriesByRegion(@Path("region") region: String): Call<List<Country>>
}