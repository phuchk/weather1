package com.example.weather.model2

import com.google.gson.annotations.SerializedName

//Serialize name
data class Rain(
    @SerializedName("1h")
    val _1h: Float
) {}