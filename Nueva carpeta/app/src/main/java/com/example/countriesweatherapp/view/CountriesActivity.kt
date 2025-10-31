package com.example.countriesweatherapp.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.countriesweatherapp.R
import com.example.countriesweatherapp.controller.CountryController
import com.example.countriesweatherapp.model.Country
import com.example.countriesweatherapp.view.adapter.CountryAdapter

class CountriesActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var progressBar: ProgressBar
    private lateinit var countryController: CountryController
    private var region: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_countries)

        region = intent.getStringExtra("REGION") ?: ""
        supportActionBar?.title = "Países de $region"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        listView = findViewById(R.id.listViewCountries)
        progressBar = findViewById(R.id.progressBar)

        countryController = CountryController()

        loadCountries()
    }

    private fun loadCountries() {
        progressBar.visibility = View.VISIBLE
        listView.visibility = View.GONE

        countryController.getCountriesByRegion(
            region = region,
            onSuccess = { countries ->
                progressBar.visibility = View.GONE
                listView.visibility = View.VISIBLE
                setupCountriesList(countries)
            },
            onError = { error ->
                progressBar.visibility = View.GONE
                Toast.makeText(this, error, Toast.LENGTH_LONG).show()
            }
        )
    }

    private fun setupCountriesList(countries: List<Country>) {
        val adapter = CountryAdapter(this, countries)
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedCountry = countries[position]
            val intent = Intent(this, CountryDetailActivity::class.java)
            intent.putExtra("COUNTRY_NAME", selectedCountry.name.common)
            intent.putExtra("COUNTRY_OFFICIAL_NAME", selectedCountry.name.official)
            intent.putExtra("COUNTRY_CAPITAL", selectedCountry.capital?.firstOrNull() ?: "")
            intent.putExtra("COUNTRY_REGION", selectedCountry.region)
            intent.putExtra("COUNTRY_SUBREGION", selectedCountry.subregion ?: "N/A")
            intent.putExtra("COUNTRY_POPULATION", selectedCountry.population)
            intent.putExtra("COUNTRY_FLAG", selectedCountry.flags.png)
            intent.putExtra("COUNTRY_CCA2", selectedCountry.cca2)
            intent.putExtra("COUNTRY_CCA3", selectedCountry.cca3)

            // Convertir monedas a String
            val currencies = selectedCountry.currencies?.values?.joinToString(", ") {
                "${it.name} (${it.symbol})"
            } ?: "N/A"
            intent.putExtra("COUNTRY_CURRENCIES", currencies)

            // Convertir idiomas a String
            val languages = selectedCountry.languages?.values?.joinToString(", ") ?: "N/A"
            intent.putExtra("COUNTRY_LANGUAGES", languages)

            // Coordenadas
            val lat = selectedCountry.latlng?.getOrNull(0) ?: 0.0
            val lng = selectedCountry.latlng?.getOrNull(1) ?: 0.0
            intent.putExtra("COUNTRY_LAT", lat)
            intent.putExtra("COUNTRY_LNG", lng)

            startActivity(intent)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}