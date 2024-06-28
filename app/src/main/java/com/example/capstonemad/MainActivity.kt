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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import com.example.capstonemad.routeApi.TravelMode
import com.example.capstonemad.routeApi.drawRoute
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher =
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
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val viewModel: MapViewModel by viewModels()


    @OptIn(ExperimentalMaterial3Api::class)
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)


        askPermission()
        setContent {
            val latLngList = remember { mutableStateListOf<LatLng>() }
            var startLocation by remember { mutableStateOf<SearchLocation?>(null) }
            var destination by remember { mutableStateOf<SearchLocation?>(null) }
            var showSearchBar by remember { mutableStateOf(true) }

            val bottomSheet = rememberBottomSheetScaffoldState()
            val scope = rememberCoroutineScope()

            var showRouteSheet by remember { mutableStateOf(false) }

            var travelMode by remember { mutableStateOf(TravelMode.Walk) }

            fun openRouteSheet() {
                drawRoute(
                    startLocation = startLocation?.location,
                    destination = destination?.location,
                    travelMode = travelMode
                ) { points ->
                    latLngList.clear()
                    latLngList.addAll(points)
                }
                scope.launch {
                    showSearchBar = false
                    showRouteSheet = true
                    bottomSheet.bottomSheetState.expand()
                }
            }

            fun updateRoute(latLng: LatLng, locationName: String) {
                showSearchBar = false
                showRouteSheet = false

                destination = SearchLocation(latLng, locationName)
                val lastLocation = viewModel.state.value.lastKnownLocation
                if (lastLocation == null || startLocation != null) {
                    openRouteSheet()
                    return
                }

                val locationLatLng = LatLng(lastLocation.latitude, lastLocation.longitude)

                geocodeLocation(this, locationLatLng) {
                    startLocation = SearchLocation(locationLatLng, it)
                    openRouteSheet()
                }
            }

            fun updateRoute(latLng: LatLng) {
                geocodeLocation(this, latLng) {
                    updateRoute(latLng, it)
                }
            }

            MapScreen(
                state = viewModel.state.value,
                latLngList = latLngList,
                startLocation = startLocation?.location,
                destination = destination?.location,
                onMapClick = { updateRoute(it) }
            )

            fun updateRoute(locationName: String) {
                geocodeLocation(this, locationName) {
                    if (it == null) return@geocodeLocation
                    updateRoute(it, locationName)
                }
            }

            if (showSearchBar) {
                SearchBar(onSearch = { updateRoute(it) })
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
                travelMode = travelMode,
                onPlanRoute = { start, end, travelingMode ->
                    travelMode = travelingMode
                    if (start.isEmpty()) {
                        updateRoute(end)
                        showRouteSheet = false
                        return@RouteModalSheet
                    }
                    geocodeLocation(this, start) {
                        startLocation = SearchLocation(it!!, start)
                        updateRoute(end)
                        showRouteSheet = false
                    }
                }
            )
        }
    }
}