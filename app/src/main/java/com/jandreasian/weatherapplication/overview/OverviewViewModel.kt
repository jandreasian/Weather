package com.jandreasian.weatherapplication.overview

import android.app.Application
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.google.android.gms.location.LocationServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.location.Geocoder
import com.jandreasian.weatherapplication.R
import com.jandreasian.weatherapplication.network.*
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date



class OverviewViewModel(application: Application) : AndroidViewModel(application) {

    // The internal MutableLiveData String that stores the status of the most recent request

    private val locationData = LocationService(application)

    private var fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)

    private var geocoder = Geocoder(application.applicationContext, Locale.getDefault())

    private lateinit var latLong: String

    var isLoading = MutableLiveData<Boolean>()

    private val _weather = MutableLiveData<Weather>()
    val weather: LiveData<Weather>
        get() = _weather

    private val _address = MutableLiveData<String>()
    val address: LiveData<String>
        get() = _address

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
                val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                val cityName = addresses[0].locality
                val stateName = addresses[0].adminArea
                latLong = location.latitude.toString() + ", " + location.longitude.toString()
                DarkSkyApi.retrofitService.getWeather(latLong).enqueue(object: Callback<Weather> {
                    override fun onFailure(call: Call<Weather>, t: Throwable) {
                        Log.e("OverViewModel: ", t.message);
                    }

                    override fun onResponse(call: Call<Weather>, response: Response<Weather>) {
                        isLoading.value = true
                        _weather.value = response.body()
                        _address.value = cityName + ", " + stateName
                    }
                })
            }
    }

    val displayTemperature = Transformations.map(weather) {
        application.applicationContext.getString(R.string.display_temp, it.currently.temperature)
    }

    val iconImg = Transformations.map(weather) {
        when(it.currently.icon) {
            "clear-day" -> ContextCompat.getDrawable(getApplication(), R.drawable.ic_clear_day)
            "clear-night"-> ContextCompat.getDrawable(getApplication(), R.drawable.ic_clear_night)
            "rain"-> ContextCompat.getDrawable(getApplication(), R.drawable.ic_rain)
            "snow"-> ContextCompat.getDrawable(getApplication(), R.drawable.ic_snow)
            "sleet"-> ContextCompat.getDrawable(getApplication(), R.drawable.ic_snow)
            "wind"-> ContextCompat.getDrawable(getApplication(), R.drawable.ic_cloudy)
            "fog"-> ContextCompat.getDrawable(getApplication(), R.drawable.ic_fog)
            "cloudy"-> ContextCompat.getDrawable(getApplication(), R.drawable.ic_cloudy)
            "partly-cloudy-day"-> ContextCompat.getDrawable(getApplication(), R.drawable.ic_partly_cloudy_day)
            "partly-cloudy-night"-> ContextCompat.getDrawable(getApplication(), R.drawable.ic_partly_cloudy_night)
            else -> ContextCompat.getDrawable(getApplication(), R.drawable.ic_clear_day)
        }
    }

    val displayHighLow = Transformations.map(weather) {
        application.applicationContext.getString(R.string.highLow, it.daily.data[0].temperatureMin, it.daily.data[0].temperatureMax)
    }

    val displayDateTime = Transformations.map(weather) {
        val date = Date(it.currently.time * 1000)
        val sdf = SimpleDateFormat("EEE, MMM d h:mm aaa", Locale.US)
        val formattedTime = sdf.format(date)
        formattedTime
    }
}
