package com.jandreasian.weatherapplication.network

data class WeatherProperty(
    val latitude: String,
    val longitude: String,
    val timezone: String,
    val currently: Currently
)

data class Currently (
    val time: Int,
    val summary: String,
    val icon: String,
    val temperature: Double
)