package com.jandreasian.weatherapplication.network

import com.jandreasian.weatherapplication.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

private const val BASE_URL = "https://api.darksky.net/forecast/" + BuildConfig.ApiKey + "/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface DarkSkyApiService {
    @GET("{latlong}")
    fun getWeather(@Path(value = "latlong") latlong: String):
            Call<Weather>
}

object DarkSkyApi {
    val retrofitService : DarkSkyApiService by lazy {
        retrofit.create(DarkSkyApiService::class.java)
    }
}