package com.example.md_lab09__activitycycle_n_statemanagement

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: Adapter
    private val tag = "MainActivity"
    private var weatherDataJson: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        val rView: RecyclerView = findViewById(R.id.rView)
        val adapter = Adapter(DiffCallback())

        setSupportActionBar(toolbar)
        rView.adapter = adapter
        rView.layoutManager = LinearLayoutManager(this)

        if (savedInstanceState == null) {
            fetchWeatherData()
        } else {
            weatherDataJson = savedInstanceState.getString("weatherData")
            Log.d(tag, "$weatherDataJson")
        }
    }

    private fun fetchWeatherData() {
        val mService: RetrofitServices = Common.retrofitService
        val lat = 54.2021736
        val lon = 30.2964015
        val appId = "your api key"

        mService.getWeatherList(lat, lon, appId).enqueue(object : Callback<WeatherWrapper> {
            override fun onResponse(call: Call<WeatherWrapper>, response: Response<WeatherWrapper>) {
                if (response.isSuccessful) {
                    val forecast = response.body()
                    adapter.submitList(forecast!!.list)
                }
            }

            override fun onFailure(call: Call<WeatherWrapper>, t: Throwable) {
                Log.e(tag, "Error fetching weather data", t)
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("weatherData", weatherDataJson)
        Log.d(tag, "$weatherDataJson")
    }
}