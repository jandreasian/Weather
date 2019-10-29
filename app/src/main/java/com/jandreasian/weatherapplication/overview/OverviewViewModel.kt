package com.jandreasian.weatherapplication.overview

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jandreasian.weatherapplication.network.DarkSkyApi
import com.jandreasian.weatherapplication.network.WeatherProperty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OverviewViewModel : ViewModel() {

    // The internal MutableLiveData String that stores the status of the most recent request
    private val _response = MutableLiveData<String>()

    // The external immutable LiveData for the request status String
    val response: LiveData<String>
        get() = _response

    /**
     * Call getMarsRealEstateProperties() on init so we can display status immediately.
     */
    init {
        getWeather()
    }

    /**
     * Sets the value of the status LiveData to the Mars API status.
     */
    private fun getWeather() {
        DarkSkyApi.retrofitService.getWeather().enqueue(object: Callback<WeatherProperty> {
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
