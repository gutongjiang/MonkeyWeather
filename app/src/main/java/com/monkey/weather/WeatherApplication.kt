package com.monkey.weather

import android.app.Application
import android.content.Context

class WeatherApplication : Application() {

    companion object {
        lateinit var context: Context
        const val TOKEN = "wWAUH4UaJXyyHHKU"
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

}