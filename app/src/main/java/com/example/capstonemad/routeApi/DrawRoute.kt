package com.example.capstonemad.routeApi

import android.util.Log
import com.example.capstonemad.BuildConfig
import com.example.capstonemad.displayString
import com.example.capstonemad.tryDisplayString
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.PolyUtil
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response

fun drawRoute(
    startLocation: LatLng?,
    destination: LatLng?,
    travelMode: TravelMode,
    onRouteDrawn: (List<LatLng>) -> Unit
) {
    if (destination == null || startLocation == null) {
        return
    }

    val departureTime = "now"
    val apiKey = BuildConfig.MAPS_API_KEY

    val startString = startLocation.tryDisplayString()
    val destinationString = destination.tryDisplayString()

    val service = ApiClient.getClient().create(RoutesApiService::class.java)
    val call =
        service.getDirections(startString, destinationString, apiKey, travelMode.travelName, departureTime)

    call.enqueue(object : Callback<DirectionsResponse> {
        override fun onResponse(
            call: Call<DirectionsResponse>,
            response: Response<DirectionsResponse>
        ) {
            if (response.isSuccessful) {
                val directionsResponse = response.body()
                if (directionsResponse != null) {
                    val polylinePoints = mutableListOf<LatLng>()
                    directionsResponse.routes.firstOrNull()?.legs?.forEach { leg ->
                        leg.steps.forEach { step ->
                            polylinePoints.addAll(PolyUtil.decode(step.polyline.points))
                        }
                    }
                    onRouteDrawn(polylinePoints)
                }
            }
        }

        override fun onFailure(call: Call<DirectionsResponse>, t: Throwable) {
            Log.e("Route", "Failure: ${t.message}")
        }

    })
}

