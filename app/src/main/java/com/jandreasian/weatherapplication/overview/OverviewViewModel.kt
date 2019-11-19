package com.jandreasian.weatherapplication.overview

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jandreasian.weatherapplication.network.DarkSkyApi
import com.jandreasian.weatherapplication.network.LocationService
import com.jandreasian.weatherapplication.network.WeatherProperty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class OverviewViewModel(application: Application) : AndroidViewModel(application) {

    // The internal MutableLiveData String that stores the status of the most recent request
    private val _response = MutableLiveData<String>()

    private val locationData = LocationService(application)

    // The external immutable LiveData for the request status String
    val response: LiveData<String>
        get() = _response

    init {
        getWeather()
    }

    fun getLocationData() = locationData

    /**
     * Sets the value of the status LiveData to the DarkSky API status.
     */
    private fun getWeather() {
        DarkSkyApi.retrofitService.getWeather("37.8267,-155.4233").enqueue(object: Callback<WeatherProperty> {
            override fun onFailure(call: Call<WeatherProperty>, t: Throwable) {
                _response.value = "Failure: " + t.message
                Log.e("OverViewModel ", t.message);
            }

            override fun onResponse(call: Call<WeatherProperty>, response: Response<WeatherProperty>) {
                _response.value = response.body()?.latitude + " " + response.body()?.longitude + " " + response.body()?.timezone;
            }
        })
    }
}
