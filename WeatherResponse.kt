package com.example.weatherapp

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    val name: String,
    val main: Main,
    val weather: List<Weather>
)

data class Main(
    val temp: Double,
    @SerializedName("feels_like")
    val feelsLike: Double,
    val humidity: Int
)

data class Weather(
    val main: String,
    val description: String,
    val icon: String
)
