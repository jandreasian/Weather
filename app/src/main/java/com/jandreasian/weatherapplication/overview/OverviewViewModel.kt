package com.jandreasian.weatherapplication.overview

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.LocationServices
import com.jandreasian.weatherapplication.network.DarkSkyApi
import com.jandreasian.weatherapplication.network.LocationService
import com.jandreasian.weatherapplication.network.WeatherProperty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class OverviewViewModel(application: Application) : AndroidViewModel(application) {

    // The internal MutableLiveData String that stores the status of the most recent request

    private val locationData = LocationService(application)

    private var fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)

    private lateinit var latLong: String

    private val _summary = MutableLiveData<String>()
    val summary: LiveData<String>
        get() = _summary

    private val _temp = MutableLiveData<String>()
    val temp: LiveData<String>
        get() = _temp


    init {
        getWeather()
    }

    fun getLocationData() = locationData

    /**
     * Sets the value of the status LiveData to the DarkSky API status.
     */
    fun getWeather() {
        fusedLocationClient!!.lastLocation
            .addOnSuccessListener { location ->
                latLong = location.latitude.toString() + ", " + location.longitude.toString()
                DarkSkyApi.retrofitService.getWeather(latLong).enqueue(object: Callback<WeatherProperty> {
                    override fun onFailure(call: Call<WeatherProperty>, t: Throwable) {
                        Log.e("OverViewModel: ", t.message);
                    }

                    override fun onResponse(call: Call<WeatherProperty>, response: Response<WeatherProperty>) {
                        _summary.value = response.body()?.currently?.summary
                        _temp.value = response.body()?.currently?.temperature.toString()
                        Log.d("OverViewModelLatLong:", latLong);
                    }
                })
            }
    }
}
