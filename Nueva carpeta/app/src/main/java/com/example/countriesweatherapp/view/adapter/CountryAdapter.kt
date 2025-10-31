package com.example.countriesweatherapp.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.countriesweatherapp.R
import com.example.countriesweatherapp.model.Country

class CountryAdapter(
    context: Context,
    private val countries: List<Country>
) : ArrayAdapter<Country>(context, 0, countries) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var itemView = convertView
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(
                R.layout.item_country,
                parent,
                false
            )
        }

        val country = countries[position]

        val flagImageView = itemView!!.findViewById<ImageView>(R.id.imageViewFlag)
        val nameTextView = itemView.findViewById<TextView>(R.id.textViewCountryName)
        val capitalTextView = itemView.findViewById<TextView>(R.id.textViewCapital)

        nameTextView.text = country.name.common
        capitalTextView.text = "Capital: ${country.capital?.firstOrNull() ?: "N/A"}"

        Glide.with(context)
            .load(country.flags.png)
            .into(flagImageView)

        return itemView
    }
}