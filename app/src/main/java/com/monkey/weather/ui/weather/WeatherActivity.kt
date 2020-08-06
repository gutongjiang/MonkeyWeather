package com.monkey.weather.ui.weather

import android.content.Context
import android.graphics.Color
import android.hardware.input.InputManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethod
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.monkey.weather.R
import com.monkey.weather.logic.model.Weather
import com.monkey.weather.logic.model.getSky
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.android.synthetic.main.forecast.*
import kotlinx.android.synthetic.main.life_index.*
import kotlinx.android.synthetic.main.now.*
import java.text.SimpleDateFormat
import java.util.*

class WeatherActivity : AppCompatActivity() {

    val viewModel by lazy {
        ViewModelProviders.of(this).get(WeatherViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 沉浸式状态栏
        val decorView = window.decorView
        decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.statusBarColor = Color.TRANSPARENT
        setContentView(R.layout.activity_weather)

        if (viewModel.lng.isEmpty()) {
            viewModel.lng = intent.getStringExtra("lng") ?: ""
        }
        if (viewModel.lat.isEmpty()) {
            viewModel.lat = intent.getStringExtra("lat") ?: ""
        }
        if (viewModel.place.isEmpty()) {
            viewModel.place = intent.getStringExtra("place") ?: ""
        }

        viewModel.weatherLiveData.observe(this, Observer {
            val weather = it.getOrNull()
            if (weather != null) {
                showWeatherInfo(weather)
            } else {
                Toast.makeText(this, "无法获取天气数据", Toast.LENGTH_SHORT).show()
                it.exceptionOrNull()?.printStackTrace()
            }
            swipeRefresh.isRefreshing = false
        })

        swipeRefresh.setOnRefreshListener {
            refresh()
        }
        refresh()

        navBtn.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerStateChanged(newState: Int) {}
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}
            override fun onDrawerOpened(drawerView: View) {}
            override fun onDrawerClosed(drawerView: View) {
                // 关闭软键盘
                val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(
                    drawerView.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
            }
        })
    }

    fun refresh() {
        viewModel.refreshWeather(viewModel.lng, viewModel.lat)
        swipeRefresh.isRefreshing = true
    }

    private fun showWeatherInfo(weather: Weather) {
        placeName.text = viewModel.place
        val realtime = weather.realtime
        val daily = weather.daily

        // 填充now.xml布局中数据
        val currentTempText = "${realtime.temperature.toInt()} ℃"
        currentTemp.text = currentTempText
        currentSky.text = getSky(realtime.skycon).info
        val currentPM25Text = "空气指数 ${realtime.airQuality.aqi.chn.toInt()}"
        currentAQI.text = currentPM25Text
        nowLayout.setBackgroundResource(getSky(realtime.skycon).bg)

        // 填充forecast.xml布局中的数据
        forecastLayout.removeAllViews()
        val days = daily.skycon.size
        for (i in 0 until days) {
            val skycon = daily.skycon[i]
            val temperature = daily.temperature[i]
            val view =
                LayoutInflater.from(this).inflate(R.layout.item_forecast, forecastLayout, false)
            val dateInfo = view.findViewById(R.id.dateInfo) as TextView
            val skyIcon = view.findViewById(R.id.skyIcon) as ImageView
            val skyInfo = view.findViewById(R.id.skyInfo) as TextView
            val temperatureInfo = view.findViewById(R.id.temperatureInfo) as TextView
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            dateInfo.text = simpleDateFormat.format(skycon.date)
            val sky = getSky(skycon.value)
            skyIcon.setImageResource(sky.icon)
            skyInfo.text = sky.info
            val tempText = "${temperature.min.toInt()} ~ ${temperature.max.toInt()} ℃"
            temperatureInfo.text = tempText
            forecastLayout.addView(view)
        }

        // 填充life_index.xml布局中的数据
        val lifeIndex = daily.lifeIndex
        coldRiskText.text = lifeIndex.coldRisk[0].desc
        dressingText.text = lifeIndex.dressing[0].desc
        ultravioletText.text = lifeIndex.ultraviolet[0].desc
        carWashingText.text = lifeIndex.carWashing[0].desc
        weatherLayout.visibility = View.VISIBLE
    }

}