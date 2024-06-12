package com.example.capstonemad.RouteApi

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RoutesApiService {
    @GET("directions/json")
    fun getDirections(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("key") apiKey: String,
        @Query("mode") mode : String,
        @Query("departureTime") departureTime : String
    ): Call<DirectionsResponse>
}