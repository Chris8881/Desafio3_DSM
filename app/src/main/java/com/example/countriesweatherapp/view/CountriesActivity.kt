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
import java.util.Locale

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

            val intent = Intent(this, CountryDetailActivity::class.java).apply {
                putExtra("COUNTRY_NAME", selectedCountry.name.common)
                putExtra("COUNTRY_OFFICIAL_NAME", selectedCountry.name.official)
                putExtra("COUNTRY_CAPITAL", selectedCountry.capital?.firstOrNull() ?: "")
                putExtra("COUNTRY_REGION", selectedCountry.region)
                putExtra("COUNTRY_SUBREGION", selectedCountry.subregion ?: "N/A")
                putExtra("COUNTRY_POPULATION", selectedCountry.population)
                putExtra("COUNTRY_FLAG", buildFlagUrl(selectedCountry))
                putExtra("COUNTRY_CCA2", selectedCountry.cca2)
                putExtra("COUNTRY_CCA3", selectedCountry.cca3)

                // Monedas
                val currencies = selectedCountry.currencies?.values?.joinToString(", ") {
                    val sym = it.symbol?.let { s -> " ($s)" } ?: ""
                    "${it.name}$sym"
                } ?: "N/A"
                putExtra("COUNTRY_CURRENCIES", currencies)

                // Idiomas
                val languages = selectedCountry.languages?.values?.joinToString(", ") ?: "N/A"
                putExtra("COUNTRY_LANGUAGES", languages)

                // Coordenadas
                val lat = selectedCountry.latlng?.getOrNull(0) ?: 0.0
                val lng = selectedCountry.latlng?.getOrNull(1) ?: 0.0
                putExtra("COUNTRY_LAT", lat)
                putExtra("COUNTRY_LNG", lng)
            }

            startActivity(intent)
        }
    }

    private fun buildFlagUrl(country: Country): String {
        val png = country.flags?.png.orEmpty()
        val svg = country.flags?.svg.orEmpty()
        val cca2 = country.cca2.orEmpty()

        if (png.isNotBlank()) return png

        if (svg.isNotBlank() && cca2.isNotBlank()) {
            return "https://flagcdn.com/w320/${cca2.lowercase(Locale.ROOT)}.png"
        }

        if (cca2.isNotBlank()) {
            return "https://flagcdn.com/w320/${cca2.lowercase(Locale.ROOT)}.png"
        }

        return svg
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
