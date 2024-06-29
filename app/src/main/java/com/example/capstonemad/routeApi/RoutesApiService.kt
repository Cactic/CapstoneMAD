package com.example.capstonemad.routeApi

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RoutesApiService {
    @GET("directions/json")
    fun getDirections(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("key") apiKey: String,
        @Query("mode") travelMode : String,
        @Query("departureTime") departureTime : String
    ): Call<DirectionsResponse>
}

enum class TravelMode {
    Drive("DRIVING"),
    Bike("BICYCLING"),
    Walk("WALKING");

    var travelName : String = ""

    constructor()
    constructor(travelName : String){
        this.travelName = travelName
    }
}