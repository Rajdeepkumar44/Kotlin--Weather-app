package com.example.weatherapp


class WeatherRepository(private val apiService: WeatherApiService) {

    suspend fun getWeather(city: String, apiKey: String, units: String) =
        apiService.getWeather(city, apiKey, units)
}
