package com.example.capstonemad

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    state: MapState,
    latLngList: MutableList<LatLng>,
    startLocation: LatLng?,
    destination: LatLng?,
    onMapClick: (LatLng) -> Unit,
) {

    var maptype by remember { mutableStateOf(MapType.NORMAL) }
    val mapProperties =
        MapProperties(
            isMyLocationEnabled = state.lastKnownLocation != null,
            mapType = maptype
        )

    val markerState = rememberMarkerState(position = LatLng(0.0, 0.0))

    val cameraPositionState = rememberCameraPositionState()
    val zoominAmount = 12f

    val sheetState = rememberModalBottomSheetState()
    var isSheetOpen by rememberSaveable {
        mutableStateOf(false)
    }

    // Effect to make the camera zoom to the new location and place a marker
    LaunchedEffect(destination) {
        destination?.let {
            markerState.position = it
            cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(it, zoominAmount))
        }
    }

    Box(modifier = Modifier.fillMaxSize())
    {
        GoogleMap(
            uiSettings = MapUiSettings(myLocationButtonEnabled = false),
            modifier = Modifier.fillMaxSize(),
            properties = mapProperties,
            cameraPositionState = cameraPositionState,
            onMapClick = onMapClick

        ) {

            if (destination != null) {
                Marker(state = MarkerState(position = destination))
            }

            if (latLngList.size != 0) {
                Polyline(points = latLngList.toList())
            } else if (startLocation != null && destination != null) {
                Polyline(
                    points = listOf(
                        startLocation,
                        destination
                    )
                )
            }
        }

    }

    Box(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f)

    ) {
        FloatingActionButton(
            onClick =
            { isSheetOpen = true },
            Modifier
                .size(40.dp)
                .align(Alignment.CenterEnd)
                .padding(0.dp, 0.dp, 10.dp, 0.dp)
                .background(Color.Transparent),
            containerColor = Color.Transparent,
            elevation = FloatingActionButtonDefaults.elevation(0.dp)
        ) {
            Icon(
                painterResource(id = R.drawable.stack_icon),
                contentDescription = "Sheet button",
            )
        }
        if (isSheetOpen) {
            ModalBottomSheet(
                sheetState = sheetState,
                onDismissRequest = { isSheetOpen = false },
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                )
                {
                    Text(text = "_________________________________________")
                    Row {
                        MapStyleButton(
                            MapType.NORMAL,
                            onMapTypeChange = { mapStyle -> maptype = mapStyle },
                            painterResource(id = R.drawable.normal),
                        )
                        MapStyleButton(
                            MapType.HYBRID,
                            onMapTypeChange = { mapStyle -> maptype = mapStyle },
                            painterResource(id = R.drawable.hybrid),
                        )
                        MapStyleButton(
                            MapType.SATELLITE,
                            onMapTypeChange = { mapStyle -> maptype = mapStyle },
                            painterResource(id = R.drawable.satellite),
                        )
                        MapStyleButton(
                            MapType.TERRAIN,
                            onMapTypeChange = { mapStyle -> maptype = mapStyle },
                            painterResource(id = R.drawable.terrain),
                        )
                    }
                    Text(text = "_________________________________________")

                    Spacer(
                        modifier = Modifier
                            .size(40.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun MapStyleButton(
    mapstyle: MapType,
    onMapTypeChange: (MapType) -> Unit,
    image: Painter,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
            .padding(0.dp, 10.dp, 20.dp, 0.dp)
    ) {
        val mapStyleText = mapstyle.toString().lowercase()

        FloatingActionButton(
            onClick = { onMapTypeChange(mapstyle) },
            modifier = Modifier
                .size(65.dp, 65.dp),
            containerColor = Color.Transparent
        ) {
            Image(image, mapStyleText)
        }
        Text(mapStyleText)
    }
}