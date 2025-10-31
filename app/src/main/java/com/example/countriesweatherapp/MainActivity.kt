package com.example.countriesweatherapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.countriesweatherapp.R
import com.example.countriesweatherapp.view.RegionsActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Redirigir inmediatamente a RegionsActivity
        val intent = Intent(this, RegionsActivity::class.java)
        startActivity(intent)
        finish()
    }
}