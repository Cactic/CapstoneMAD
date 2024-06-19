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
import androidx.compose.material3.SheetValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import com.example.capstonemad.routeApi.TravelMode
import com.example.capstonemad.routeApi.drawRoute
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
            val latLngList = remember { mutableStateListOf<LatLng>() }
            var startLocation by remember { mutableStateOf<SearchLocation?>(null) }
            var destination by remember { mutableStateOf<SearchLocation?>(null) }
            var showSearchBar by remember { mutableStateOf(true) }

            val bottomSheet = rememberBottomSheetScaffoldState()
            val scope = rememberCoroutineScope()

            var showRouteSheet by remember { mutableStateOf(false) }

            fun UpdateRoute(locationName: String) {
                geocodeLocation(this, locationName) { destinationLatLng ->
                    fun drawMyRoute() {
                        drawRoute(
                            startLocation = startLocation?.location,
                            destination = destination?.location,
                            travelMode = TravelMode.Drive
                        ) {
                            latLngList.clear()
                            latLngList.addAll(it)
                        }
                        scope.launch {
                            showSearchBar = false
                            showRouteSheet = true
                            bottomSheet.bottomSheetState.expand()

                        }
                    }
                    searchedLocation = destinationLatLng
                    if (destinationLatLng != null) {
                        destination = SearchLocation(
                            LatLng(
                                destinationLatLng.latitude,
                                destinationLatLng.longitude
                            ), locationName
                        )
                        val lastLocation = viewModel.state.value.lastKnownLocation
                        if (lastLocation != null && startLocation == null) {
                            val locationLatLng =
                                LatLng(lastLocation.latitude, lastLocation.longitude)
                            geocodeLocation(this, locationLatLng) {
                                startLocation = SearchLocation(locationLatLng, it)
                                drawMyRoute()
                            }
                        }
                    } else {
                        drawMyRoute()
                    }
                }
            }

            MapScreen(
                state = viewModel.state.value,
                searchedLocation = searchedLocation,
                latLngList = latLngList,
                startLocation = startLocation?.location,
                destination = destination?.location,
                onMapClick = {
                    //startLocation = viewModel.state.value.lastKnownLocation
                    destination = SearchLocation(it, it.displayString())
                    scope.launch {
                        showRouteSheet = true
                        showSearchBar = false
                        bottomSheet.bottomSheetState.expand()
                    }
                }
            )

            if (showSearchBar) {
                SearchBar(onSearch = {UpdateRoute(it)})
            }
            LaunchedEffect(bottomSheet.bottomSheetState) {
                snapshotFlow { bottomSheet.bottomSheetState.currentValue }.collect { currentValue ->
                    if (currentValue == SheetValue.PartiallyExpanded) {
                        showRouteSheet = false
                        showSearchBar = true;
                    }
                }
            }
            RouteModalSheet(
                bottomSheetState = bottomSheet,
                showRouteSheet = showRouteSheet,
                startLocation = startLocation?.name,
                destination = destination?.name,
                travelmode = TravelMode.Drive
            )
        }
    }

}