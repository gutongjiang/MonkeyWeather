package com.monkey.weather.logic.model

import com.google.gson.annotations.SerializedName

class RealtimeResponse(val status: String, val result: Result) {

    class AQI(val chn: Float)

    class AirQuality(val aqi: AQI)

    class Result(val realtime: Realtime)

    class Realtime(
        val skycon: String,
        val temperature: Float, @SerializedName("air_quality") val airQuality: AirQuality
    )

}