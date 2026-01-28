package com.example.weatherapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


class WeatherViewModel : ViewModel() {

    private val weatherRepository = WeatherRepository(WeatherApiClient.apiService)
    private val _weatherData = MutableLiveData<WeatherResponse?>()
    val weatherData: LiveData<WeatherResponse?> = _weatherData
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun fetchWeatherData(city: String) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            _errorMessage.postValue(null)
            _weatherData.postValue(null)

            try {

                val response = weatherRepository.getWeather(city, BuildConfig.WEATHER_API_KEY,"metric")

                if (response.isSuccessful && response.body() != null) {
                    _weatherData.postValue(response.body())
                } else {
                    _errorMessage.postValue("Error: ${response.message()} (Code: ${response.code()})")
                }
            } catch (e: Exception) {
                _errorMessage.postValue("Network Error: Please check your connection.")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
}
