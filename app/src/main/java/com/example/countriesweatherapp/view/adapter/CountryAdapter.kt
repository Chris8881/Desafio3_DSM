package com.example.countriesweatherapp.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import coil.decode.SvgDecoder
import coil.load
import com.example.countriesweatherapp.R
import com.example.countriesweatherapp.model.Country

class CountryAdapter(
    context: Context,
    private val countries: List<Country>
) : ArrayAdapter<Country>(context, 0, countries) {

    private data class ViewHolder(
        val flagImage: ImageView,
        val nameText: TextView,
        val capitalText: TextView
    )

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val itemView: View
        val holder: ViewHolder

        if (convertView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.item_country, parent, false)
            holder = ViewHolder(
                flagImage = itemView.findViewById(R.id.imageViewFlag),
                nameText = itemView.findViewById(R.id.textViewCountryName),
                capitalText = itemView.findViewById(R.id.textViewCapital)
            )
            itemView.tag = holder
        } else {
            itemView = convertView
            holder = convertView.tag as ViewHolder
        }

        val country = countries[position]

        holder.nameText.text = country.name.common
        val capitalLabel = context.getString(R.string.label_capital)
        holder.capitalText.text = "$capitalLabel ${country.capital?.firstOrNull() ?: "N/A"}"

        val url = country.flags?.png ?: country.flags?.svg ?: ""

        holder.flagImage.load(url) {
            crossfade(true)
            placeholder(R.drawable.placeholder_rect)
            error(R.drawable.placeholder_rect)
            decoderFactory(SvgDecoder.Factory())
        }

        return itemView
    }
}
