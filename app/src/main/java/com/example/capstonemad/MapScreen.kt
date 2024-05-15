package com.example.capstonemad

import androidx.compose.runtime.Composable
import android.content.Context
import android.location.Location
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.capstonemad.MapState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.FloatingActionButtonElevation
import androidx.compose.ui.draw.shadow
import com.google.android.gms.maps.model.MarkerOptions

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

    val cameraPositionState = rememberCameraPositionState()
    Box(modifier = Modifier.fillMaxSize())
    {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            properties = mapProperties,
            cameraPositionState = cameraPositionState,
        ){
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
                        Column(horizontalAlignment = Alignment.CenterHorizontally ,modifier = Modifier
                            .padding(0.dp, 10.dp, 20.dp, 0.dp)){

                            FloatingActionButton(onClick = { maptype = MapType.NORMAL },
                                modifier = Modifier
                                    .size(65.dp, 65.dp),
                                containerColor = Color.Transparent){
                                Image(painterResource(id = R.drawable.normal), contentDescription = "normal mapstyle")
                            }
                            Text(text = stringResource(id = R.string.Normal))
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally ,modifier = Modifier
                            .padding(0.dp, 10.dp, 20.dp, 0.dp)) {
                            FloatingActionButton(
                                onClick = { maptype = MapType.HYBRID },
                                modifier = Modifier
                                    .size(65.dp, 65.dp),
                                containerColor = Color.Transparent
                            ) {
                                Image(
                                    painterResource(id = R.drawable.hybrid),
                                    contentDescription = "normal mapstyle"
                                )
                            }
                            Text(text = stringResource(id = R.string.Hybrid))
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally ,modifier = Modifier
                            .padding(0.dp, 10.dp, 20.dp, 0.dp)){
                            FloatingActionButton(onClick = { maptype = MapType.SATELLITE },
                            modifier = Modifier
                                .size(65.dp, 65.dp),
                            containerColor = Color.Transparent
                            ){
                            Image(painterResource(id = R.drawable.satellite), contentDescription = "normal mapstyle" )
                        }
                            Text(text = stringResource(id = R.string.Satellite))
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally ,modifier = Modifier
                            .padding(0.dp, 10.dp, 20.dp, 0.dp)) {
                            FloatingActionButton(
                                onClick = { maptype = MapType.TERRAIN },
                                modifier = Modifier
                                    .size(65.dp, 65.dp),
                                containerColor = Color.Transparent
                            ) {
                                Image(
                                    painterResource(id = R.drawable.terrain),
                                    contentDescription = "normal mapstyle"
                                )
                            }
                            Text(text = stringResource(id = R.string.Terrain))
                        }
                    }
                    Text(text = "_________________________________________")

                    Spacer(modifier = Modifier
                        .size(40.dp))
                }
            }
        }
    }
}