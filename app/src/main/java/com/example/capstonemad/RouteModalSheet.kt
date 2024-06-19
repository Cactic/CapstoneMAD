package com.example.capstonemad

import android.util.Log
import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.indication
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.capstonemad.routeApi.TravelMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteModalSheet(
    bottomSheetState: BottomSheetScaffoldState,
    showRouteSheet: Boolean,
    startLocation: String?,
    destination: String?,
    travelmode: TravelMode
) {

    var chooseRouteSheet by remember { mutableStateOf(false) }
    var selectedMode by remember { mutableStateOf(travelmode) }

    if (!showRouteSheet) {
        chooseRouteSheet = false
        return
    }

    BottomSheetScaffold(
        scaffoldState = bottomSheetState,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            if (!chooseRouteSheet) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                ) {
                    //marked name
                    FloatingActionButton(
                        modifier = Modifier
                            .padding(16.dp),
                        shape = RoundedCornerShape(24.dp),
                        onClick = { chooseRouteSheet = true },
                        containerColor = Color(173, 207, 240)
                    ) {
                        Text(
                            text = "Directions", modifier = Modifier
                                .padding(start = 12.dp, end = 12.dp),
                            fontSize = 15.sp
                        )
                    }
                }
            } else {
                Column(modifier = Modifier.fillMaxWidth()) {
                    //add a row
                    //Travel mode image
                    //Travel time
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row() {
                            TravelMode.entries.forEach {
                                TravelModeButton(
                                    travelMode = it,
                                    onClick = { selectedMode = it },
                                    selected = selectedMode == it
                                )
                            }

                        }
                        FloatingActionButton(
                            modifier = Modifier,
                            shape = RoundedCornerShape(24.dp),
                            containerColor = Color(173, 207, 240),
                            onClick = { chooseRouteSheet = true }) {
                            Row(
                                modifier = Modifier
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowUp,
                                    contentDescription = "up arrow"
                                )
                                Text(text = "Start", modifier = Modifier)
                            }
                        }
                    }


                }
            }
        }) {
        if (chooseRouteSheet) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.size(10.dp))

                    LocationField(startLocation ?: "", "Start location")
                    Spacer(modifier = Modifier.size(10.dp))
                    LocationField(destination ?: "", "destination")
                }
            }
        }
    }
}

@Composable
fun LocationField(inputValue: String, labelText: String) {
    var locationInput by remember { mutableStateOf(inputValue) }

    TextField(
        value = locationInput,
        onValueChange = { locationInput = it },
        modifier = Modifier
            .size(200.dp, 45.dp)
            .border(1.dp, Color.Black, RoundedCornerShape(25.dp)),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        label = {
            Text(text = labelText, fontSize = 12.sp)
        })
}

@Composable
fun TravelModeButton(travelMode: TravelMode, onClick: () -> Unit, selected: Boolean) {
    FloatingActionButton(
        modifier = Modifier
            .padding(end = 8.dp)
            .size(60.dp)
            .border(if (selected) 2.dp else (-1).dp, Color.Black, RoundedCornerShape(24.dp)),
        shape = RoundedCornerShape(24.dp),
        onClick = onClick
    ) {
        Row {
            Text(text = travelMode.name)
        }
    }
}