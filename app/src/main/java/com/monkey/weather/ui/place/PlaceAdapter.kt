package com.monkey.weather.ui.place

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.monkey.weather.R
import com.monkey.weather.logic.model.Place
import com.monkey.weather.ui.weather.WeatherActivity

class PlaceAdapter(private val fragment: Fragment, private val list: List<Place>) :
    RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.placeName)
        val address: TextView = view.findViewById(R.id.placeAddress)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_place, parent, false)
        val holder = ViewHolder(view)
        holder.itemView.setOnClickListener {
            val p = list[holder.absoluteAdapterPosition]
            val intent = Intent(parent.context, WeatherActivity::class.java).apply {
                putExtra("lng", p.location.lng)
                putExtra("lat", p.location.lat)
                putExtra("place", p.name)
            }
            fragment.startActivity(intent)
        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val p = list[position]
        holder.name.text = p.name
        holder.address.text = p.address
    }

    override fun getItemCount() = list.size

}