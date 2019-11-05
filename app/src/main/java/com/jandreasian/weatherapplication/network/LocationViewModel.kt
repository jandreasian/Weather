package com.jandreasian.weatherapplication.network

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class LocationViewModel(application: Application) : AndroidViewModel(application) {

    private val locationData = LocationService(application)

    fun getLocationData() = locationData
}