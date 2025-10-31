package com.example.countriesweatherapp.view

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import coil.decode.SvgDecoder
import coil.load
import com.example.countriesweatherapp.R
import com.example.countriesweatherapp.controller.WeatherController
import com.example.countriesweatherapp.model.WeatherResponse

class CountryDetailActivity : AppCompatActivity() {

    private lateinit var imageViewFlag: ImageView
    private lateinit var textViewName: TextView
    private lateinit var textViewOfficialName: TextView
    private lateinit var textViewCapital: TextView
    private lateinit var textViewRegion: TextView
    private lateinit var textViewSubregion: TextView
    private lateinit var textViewPopulation: TextView
    private lateinit var textViewCodes: TextView
    private lateinit var textViewCurrencies: TextView
    private lateinit var textViewLanguages: TextView
    private lateinit var textViewCoordinates: TextView

    // Weather views
    private lateinit var weatherContainer: View
    private lateinit var imageViewWeatherIcon: ImageView
    private lateinit var textViewTemperature: TextView
    private lateinit var textViewCondition: TextView
    private lateinit var textViewWind: TextView
    private lateinit var textViewHumidity: TextView
    private lateinit var progressBarWeather: ProgressBar

    private lateinit var weatherController: WeatherController
    private var capital: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_country_detail)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initViews()
        weatherController = WeatherController()

        loadCountryData()
        loadWeatherData()
    }

    private fun initViews() {
        // Country views
        imageViewFlag = findViewById(R.id.imageViewFlag)
        textViewName = findViewById(R.id.textViewName)
        textViewOfficialName = findViewById(R.id.textViewOfficialName)
        textViewCapital = findViewById(R.id.textViewCapital)
        textViewRegion = findViewById(R.id.textViewRegion)
        textViewSubregion = findViewById(R.id.textViewSubregion)
        textViewPopulation = findViewById(R.id.textViewPopulation)
        textViewCodes = findViewById(R.id.textViewCodes)
        textViewCurrencies = findViewById(R.id.textViewCurrencies)
        textViewLanguages = findViewById(R.id.textViewLanguages)
        textViewCoordinates = findViewById(R.id.textViewCoordinates)

        // Weather views
        weatherContainer = findViewById(R.id.weatherContainer)
        imageViewWeatherIcon = findViewById(R.id.imageViewWeatherIcon)
        textViewTemperature = findViewById(R.id.textViewTemperature)
        textViewCondition = findViewById(R.id.textViewCondition)
        textViewWind = findViewById(R.id.textViewWind)
        textViewHumidity = findViewById(R.id.textViewHumidity)
        progressBarWeather = findViewById(R.id.progressBarWeather)
    }

    private fun loadCountryData() {
        val name = intent.getStringExtra("COUNTRY_NAME") ?: ""
        val officialName = intent.getStringExtra("COUNTRY_OFFICIAL_NAME") ?: ""
        capital = intent.getStringExtra("COUNTRY_CAPITAL") ?: ""
        val region = intent.getStringExtra("COUNTRY_REGION") ?: ""
        val subregion = intent.getStringExtra("COUNTRY_SUBREGION") ?: ""
        val population = intent.getLongExtra("COUNTRY_POPULATION", 0)
        val flag = intent.getStringExtra("COUNTRY_FLAG") ?: ""   // URL original (png o svg)
        val cca2 = intent.getStringExtra("COUNTRY_CCA2") ?: ""
        val cca3 = intent.getStringExtra("COUNTRY_CCA3") ?: ""
        val currencies = intent.getStringExtra("COUNTRY_CURRENCIES") ?: ""
        val languages = intent.getStringExtra("COUNTRY_LANGUAGES") ?: ""
        val lat = intent.getDoubleExtra("COUNTRY_LAT", 0.0)
        val lng = intent.getDoubleExtra("COUNTRY_LNG", 0.0)

        supportActionBar?.title = name

        textViewName.text = "Nombre: $name"
        textViewOfficialName.text = "Nombre Oficial: $officialName"
        textViewCapital.text = "Capital: $capital"
        textViewRegion.text = "Región: $region"
        textViewSubregion.text = "Subregión: $subregion"
        textViewPopulation.text = "Población: ${formatNumber(population)}"
        textViewCodes.text = "Códigos ISO: $cca2 / $cca3"
        textViewCurrencies.text = "Monedas: $currencies"
        textViewLanguages.text = "Idiomas: $languages"
        textViewCoordinates.text = "Coordenadas: $lat, $lng"

        imageViewFlag.load(flag) {
            crossfade(true)
            placeholder(R.drawable.placeholder_rect)
            error(R.drawable.placeholder_rect)
            // 👇 CORRECTO: pasar la fábrica directamente (sin lambda)
            decoderFactory(SvgDecoder.Factory())
        }
    }

    private fun loadWeatherData() {
        if (capital.isEmpty()) {
            weatherContainer.visibility = View.GONE
            Toast.makeText(this, "No se puede obtener clima sin capital", Toast.LENGTH_SHORT).show()
            return
        }

        progressBarWeather.visibility = View.VISIBLE
        weatherContainer.visibility = View.GONE

        weatherController.getCurrentWeather(
            city = capital,
            onSuccess = { weather ->
                progressBarWeather.visibility = View.GONE
                weatherContainer.visibility = View.VISIBLE
                displayWeather(weather)
            },
            onError = { error ->
                progressBarWeather.visibility = View.GONE
                Toast.makeText(this, "Error al cargar clima: $error", Toast.LENGTH_LONG).show()
            }
        )
    }

    private fun displayWeather(weather: WeatherResponse) {
        val current = weather.current

        textViewTemperature.text = "${current.tempC}°C / ${current.tempF}°F"
        textViewCondition.text = current.condition.text
        textViewWind.text = "Viento: ${current.windKph} kph / ${current.windMph} mph"
        textViewHumidity.text = "Humedad: ${current.humidity}%"

        val iconUrl = "https:${current.condition.icon}"
        imageViewWeatherIcon.load(iconUrl) {
            crossfade(true)
            placeholder(R.drawable.placeholder_rect)
            error(R.drawable.placeholder_rect)
            decoderFactory(SvgDecoder.Factory())
        }
    }

    private fun formatNumber(number: Long): String {
        return String.format("%,d", number)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
