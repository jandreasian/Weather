package com.jandreasian.weatherapplication.network

data class Weather(
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    val currently: Currently,
    val daily: Daily
)

data class Currently (
    val time: Long,
    val summary: String,
    val icon: String,
    val temperature: Double
)

data class Daily (
    val data: List<DailyData>
)

data class DailyData (
    val temperatureMin: Double,
    val temperatureMax: Double
)