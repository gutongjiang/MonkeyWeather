package com.monkey.weather.logic

import androidx.lifecycle.liveData
import com.monkey.weather.logic.model.Place
import com.monkey.weather.logic.model.Weather
import com.monkey.weather.logic.network.WeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.coroutines.CoroutineContext

object Repository {

//    fun searchPlaces(query: String) = liveData(Dispatchers.IO) {
//        val result = try {
//            val response = WeatherNetwork.searchPlaces(query)
//            if (response.status == "ok") {
//                Result.success(response.places)
//            } else {
//                Result.failure(RuntimeException("response status is ${response.status}"))
//            }
//        } catch (e: Exception) {
//            Result.failure<List<Place>>(e)
//        }
//        emit(result)
//    }

    fun searchPlaces(query: String) = fire(Dispatchers.IO) {
        val response = WeatherNetwork.searchPlaces(query)
        if (response.status == "ok") {
            Result.success(response.places)
        } else {
            Result.failure(RuntimeException("response status is ${response.status}"))
        }
    }

    fun refreshWeather(lng: String, lat: String) = fire(Dispatchers.IO) {
        coroutineScope {
            val realtime = async {
                WeatherNetwork.getRealtime(lng, lat)
            }
            val daily = async {
                WeatherNetwork.getDaily(lng, lat)
            }
            val realtimeResponse = realtime.await()
            val dailyResponse = daily.await()
            if (realtimeResponse.status == "ok" && dailyResponse.status == "ok") {
                val weather = Weather(realtimeResponse.result.realtime, dailyResponse.result.daily)
                Result.success(weather)
            } else {
                Result.failure(
                    RuntimeException(
                        "realtime status is ${realtimeResponse.status} " + "daily status is ${dailyResponse.status}\""
                    )
                )
            }
        }
    }

    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) =
        liveData<Result<T>>(context) {
            val result = try {
                block()
            } catch (e: Exception) {
                Result.failure<T>(e)
            }
            emit(result)
        }

}