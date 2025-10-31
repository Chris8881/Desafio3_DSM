package com.example.countriesweatherapp.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.countriesweatherapp.R
import com.example.countriesweatherapp.utils.Constants

class RegionsActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regions)

        supportActionBar?.title = "Selecciona una Región"

        listView = findViewById(R.id.listViewRegions)
        progressBar = findViewById(R.id.progressBar)

        setupRegionsList()
    }

    private fun setupRegionsList() {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            Constants.REGIONS
        )

        listView.adapter = adapter

        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val selectedRegion = Constants.REGIONS[position]
            val intent = Intent(this, CountriesActivity::class.java)
            intent.putExtra("REGION", selectedRegion)
            startActivity(intent)
        }
    }
}