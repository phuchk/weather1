package com.example.weather

import com.example.weather.model2.ListWeather
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface myAPI {
    //?lat={lat}&lon={lon}&lang=vi&units=metric&appid=${RetrofitHelper.API_KEY}
    @GET("data/2.5/onecall")
    suspend fun getWeather(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("lang") lang: String,
        @Query("units") units: String,
        @Query("appid") appid: String
    ): Response<ListWeather>
//
}