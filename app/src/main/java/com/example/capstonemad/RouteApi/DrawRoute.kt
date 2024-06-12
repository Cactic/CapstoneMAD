package com.example.capstonemad.RouteApi

import android.util.Log
import com.example.capstonemad.BuildConfig
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.PolyUtil
import com.google.maps.android.ktx.model.polygonOptions
import com.google.maps.android.ktx.model.polylineOptions
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response
fun drawRoute(
    startLocation : LatLng?,
    destination: LatLng?,
    onRouteDrawn: (List<LatLng>) -> Unit
) {
    if (destination == null || startLocation == null) {
        return
    }

    val mode = "bike"
    val departureTime = "now"
    val apiKey = BuildConfig.MAPS_API_KEY

    val startString = "${startLocation.latitude},${startLocation.longitude}"
    val destinationString = "${destination.latitude},${destination.longitude}"

    val service = ApiClient.getClient().create(RoutesApiService::class.java)
    val call = service.getDirections(startString, destinationString,apiKey, mode, departureTime)

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

