package com.example.capstonemad

import androidx.compose.runtime.Composable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.maps.android.compose.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.ui.graphics.painter.Painter
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.ktx.model.markerOptions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    state: MapState,
    ) {
    var maptype by remember{ mutableStateOf(MapType.NORMAL) }
    var mapProperties =
        MapProperties(
            isMyLocationEnabled = state.lastKnownLocation != null,
            mapType = maptype
        )

    var markerState = rememberMarkerState(position = LatLng(0.0, 0.0))
    var isMarkerVisible by remember { mutableStateOf(false) }

    val cameraPositionState = rememberCameraPositionState()
    Box(modifier = Modifier.fillMaxSize())
    {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            properties = mapProperties,
            cameraPositionState = cameraPositionState,
            onMapClick = { latLng ->
                markerState.position = latLng
                isMarkerVisible = true
            }
        ) {
            if (isMarkerVisible) {
                Marker(
                    state = markerState,
                    title = "Marker",
                )
            }
        }

    }
    val sheetState = rememberModalBottomSheetState()
    var isSheetOpen by rememberSaveable {
        mutableStateOf(false)
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
                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth())
                {
                    Text(text = "_________________________________________")
                    Row {
                            ShowButton(MapType.NORMAL, onMapTypeChange = {mapStyle -> maptype = mapStyle}, painterResource(id = R.drawable.normal),"Normal", "Normal")
                            ShowButton(MapType.HYBRID,  onMapTypeChange = {mapStyle -> maptype = mapStyle}, painterResource(id = R.drawable.hybrid),"Hybrid", "Hybrid")
                            ShowButton(MapType.SATELLITE,  onMapTypeChange = {mapStyle -> maptype = mapStyle}, painterResource(id = R.drawable.satellite),"Satellite", "Satellite")
                            ShowButton(MapType.TERRAIN,  onMapTypeChange = {mapStyle -> maptype = mapStyle}, painterResource(id = R.drawable.terrain),"Terrain", "Terrain")
                    }
                    Text(text = "_________________________________________")

                    Spacer(modifier = Modifier
                        .size(40.dp))
                }
            }
        }
    }
}
@Composable
fun ShowButton(mapstyle : MapType, onMapTypeChange : (MapType) -> Unit ,image : Painter, description : String, Undertext: String){
    Column(horizontalAlignment = Alignment.CenterHorizontally ,modifier = Modifier
        .padding(0.dp, 10.dp, 20.dp, 0.dp)) {

        FloatingActionButton(
            onClick = { onMapTypeChange(mapstyle)},
            modifier = Modifier
                .size(65.dp, 65.dp),
            containerColor = Color.Transparent
        ) {
            Image(image, description)
        }
        Text(Undertext.toString())
    }
}