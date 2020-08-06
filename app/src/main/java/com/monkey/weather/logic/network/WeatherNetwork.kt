package com.monkey.weather.logic.network

import retrofit2.await

object WeatherNetwork {

    private val placeService = ServiceCreator.create<PlaceService>()

    suspend fun searchPlaces(query: String) = placeService.searchPlaces(query).await()

    suspend fun getRealtime(lng: String, lat: String) =
        placeService.getRealtimeWeather(lng, lat).await()

    suspend fun getDaily(lng: String, lat: String) =
        placeService.getDailyWeather(lng, lat).await()

}