package com.example.capstonemad

import com.google.android.gms.maps.model.LatLng

fun LatLng.displayString(): String {
    return "${this.latitude},${this.longitude}"
}

fun LatLng?.tryDisplayString(): String {
    if (this == null) {
        return ""

    }
    return "${this.latitude},${this.longitude}"
}