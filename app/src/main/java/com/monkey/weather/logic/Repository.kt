package com.monkey.weather.logic

import androidx.lifecycle.liveData
import com.monkey.weather.logic.model.Place
import com.monkey.weather.logic.network.WeatherNetwork
import kotlinx.coroutines.Dispatchers

object Repository {

    fun searchPlaces(query: String) = liveData(Dispatchers.IO) {
        val result = try {
            val response = WeatherNetwork.searchPlaces(query)
            if (response.status == "ok") {
                Result.success(response.places)
            } else {
                Result.failure(RuntimeException("response status is ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure<List<Place>>(e)
        }
        emit(result)
    }

}