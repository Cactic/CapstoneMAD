package com.example.capstonemad

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.android.gms.maps.model.LatLng
import java.util.Locale

// Function to geocode a location name to LatLng using Geocoder and GeocodeListener
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun geocodeLocation(context: Context, locationName: String, callback: (LatLng?) -> Unit) {
    // Make a geocoder instance and ensures that the geocoding results are localized for the device's default locale
    val geocoder = Geocoder(context, Locale.getDefault())

    val no_of_adresses = 1

    // Use GeocodeListener to handle the results asynchronously
    geocoder.getFromLocationName(locationName, no_of_adresses, object : Geocoder.GeocodeListener {
        override fun onGeocode(addresses: MutableList<Address>) {
            if (addresses.isNotEmpty()) {
                val address = addresses[0]
                val location = LatLng(address.latitude, address.longitude)
                callback(location)
            } else {
                callback(null)
            }
        }

        override fun onError(errorMessage: String?) {
            callback(null)
        }
    })
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun geocodeLocation(context: Context, locationLatLng: LatLng, callback: (String) -> Unit) {
    // Make a geocoder instance and ensures that the geocoding results are localized for the device's default locale
    val geocoder = Geocoder(context, Locale.getDefault())

    val no_of_adresses = 1

    val noAddressResponse = locationLatLng.displayString()

    // Use GeocodeListener to handle the results asynchronously
    geocoder.getFromLocation(
        locationLatLng.latitude,
        locationLatLng.longitude,
        no_of_adresses,
        object : Geocoder.GeocodeListener {
            override fun onGeocode(addresses: MutableList<Address>) {
                if (addresses.isNotEmpty()) {
                    val fetchedAddress = addresses[0]
                    val strAddress = StringBuilder()

                    for (i in 0 until fetchedAddress.maxAddressLineIndex) {
                        strAddress.append(fetchedAddress.getAddressLine(i)).append("\n")
                    }

                    val address = strAddress.toString()
                    if (address.isNotEmpty()) {
                        callback(address)
                    } else {
                        callback(noAddressResponse)
                    }
                } else {
                    callback(noAddressResponse)
                }
            }

            override fun onError(errorMessage: String?) {
                callback(noAddressResponse)
            }
        })
}