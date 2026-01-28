package com.example.weatherapp

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapp.databinding.ActivityMainBinding
class MainActivity : AppCompatActivity() {
    private val weatherViewModel: WeatherViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        observeViewModel()
        binding.buttonFetchWeather.setOnClickListener {
            val cityName = binding.editTextCity.text.toString().trim()
            if (cityName.isNotEmpty()) {
                weatherViewModel.fetchWeatherData(cityName)
            } else {
                Toast.makeText(this, "Please enter a city name", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun observeViewModel() {
        weatherViewModel.weatherData.observe(this) { weatherResponse ->
            weatherResponse?.let {
                updateWeatherUI(it)
            }
        }
        weatherViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.buttonFetchWeather.isEnabled = !isLoading

            if (isLoading) {
                binding.textViewError.visibility = View.GONE
                binding.weatherContentContainer.visibility = View.GONE
            }
        }
        weatherViewModel.errorMessage.observe(this) { errorMessage ->
            if (errorMessage != null) {
                binding.textViewError.text = errorMessage
                binding.textViewError.visibility = View.VISIBLE
                binding.weatherContentContainer.visibility = View.GONE
            } else {
                binding.textViewError.visibility = View.GONE
            }
        }
    }

    private fun updateWeatherUI(data: WeatherResponse) {
        binding.weatherContentContainer.visibility = View.VISIBLE
        binding.textViewCityName.text = data.name
        binding.textViewTemperature.text = getString(R.string.temperature_format, data.main.temp)
        binding.textViewWeatherCondition.text = data.weather.firstOrNull()?.main ?: "N/A"

        val iconResource = getWeatherIcon(data.weather.firstOrNull()?.icon)
        binding.imageViewWeatherIcon.setImageResource(iconResource)
    }

    private fun getWeatherIcon(iconCode: String?): Int {
        return when (iconCode) {
            "01d" -> R.drawable.sunny
            "03d", "03n", "04d", "04n" -> R.drawable.cloudy
            "09d", "09n", "10d", "10n" -> R.drawable.rainy
            else -> R.drawable.sunny

        }
    }
}
