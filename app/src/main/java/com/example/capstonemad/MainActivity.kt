package com.example.capstonemad

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.capstonemad.RouteApi.drawRoute
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {

    private val requestPermisisonLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                viewModel.getDeviceLocation(fusedLocationProviderClient)
            }
        }

    private fun askPermission() = when {
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
                == PackageManager.PERMISSION_GRANTED
        -> {
            viewModel.getDeviceLocation(fusedLocationProviderClient)
        }

        else -> {
            requestPermisisonLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    val viewModel: MapViewModel by viewModels()


    @OptIn(ExperimentalMaterial3Api::class)
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)


        askPermission()
        setContent {
            var searchedLocation by remember { mutableStateOf<LatLng?>(null) }
            var latLngList = remember { mutableStateListOf<LatLng>() }
            var startLocation = remember { mutableStateOf<LatLng?>(null) }
            var destination = remember { mutableStateOf<LatLng?>(null) }
            var showSearchBar = remember{ mutableStateOf(true) }
            var resetRouteSheet = remember { mutableStateOf(false) }

            var bottomSheet = rememberBottomSheetScaffoldState()
            val scope = rememberCoroutineScope()



            MapScreen(
                state = viewModel.state.value,
                searchedLocation = searchedLocation,
                latLngList = latLngList,
                startLocation = startLocation,
                destination = destination,
                onMapClick = { scope.launch {
                    showSearchBar.value = false
                    resetRouteSheet.value = true
                    bottomSheet.bottomSheetState.expand()
                    } }
            )
            if(showSearchBar.value) {
                SearchBar(onSearch = { locationName ->
                    geocodeLocation(this, locationName) { location ->
                        searchedLocation = location
                        if (location != null) {
                            val lastLocation = viewModel.state.value.lastKnownLocation
                            if (lastLocation != null && startLocation.value == null) {
                                startLocation.value =
                                    LatLng(lastLocation.latitude, lastLocation.longitude)
                            }
                            destination.value = (LatLng(location.latitude, location.longitude))
                        }
                        drawRoute(
                            startLocation = startLocation.value,
                            destination = destination.value
                        ) {
                            latLngList.clear()
                            latLngList.addAll(it)
                        }
                        scope.launch {
                            showSearchBar.value = false
                            resetRouteSheet.value = true
                            bottomSheet.bottomSheetState.expand()
                        }
                    }
                })
            }
            RouteModalSheet(bottomSheetState = bottomSheet, resetRouteSheet = resetRouteSheet)
        }
    }
}
