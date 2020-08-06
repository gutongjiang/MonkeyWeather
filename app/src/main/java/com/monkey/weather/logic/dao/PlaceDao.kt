package com.monkey.weather.logic.dao

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import com.monkey.weather.WeatherApplication
import com.monkey.weather.logic.model.Place

object PlaceDao {

    private fun sp() =
        WeatherApplication.context.getSharedPreferences("weather", Context.MODE_PRIVATE)

    fun save(place: Place) {
        sp().edit {
            putString("p", Gson().toJson(place))
        }
    }

    fun get(): Place {
        val p = sp().getString("p", "")
        return Gson().fromJson(p, Place::class.java)
    }

    fun isPlaceSaved() = sp().contains("p")

}