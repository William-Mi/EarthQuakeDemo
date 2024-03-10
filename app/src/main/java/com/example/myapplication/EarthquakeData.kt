package com.example.myapplication

data class EarthquakeData(
    val magnitude: Double,
    val formattedDateTime: String,
    val updatedDateTime: String,
    val location: String,
    val latitude: Double,
    val longitude: Double,
    val depth: Double
)
