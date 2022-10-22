package com.maximmesh.weathertaskapp.data

data class WeatherModel(
    val city: String,
    val time: String,
    val currentTemp: Double,
    val imageUrl: String,
    val hours: String
)