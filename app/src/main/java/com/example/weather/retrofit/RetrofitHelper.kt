package com.example.weather.retrofit

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    const val API_KEY :String= "a967dd47297960fe94f0ca8bd235a61a"

    val baseUrl = "https://api.openweathermap.org/"

    var gson: Gson = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    fun getInstance(): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

}