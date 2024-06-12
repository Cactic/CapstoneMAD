package com.example.capstonemad

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteModalSheet(
    bottomSheetState: BottomSheetScaffoldState,
    resetRouteSheet: MutableState<Boolean>,
) {

    var chooseRouteSheet by remember { mutableStateOf(false) }

    if(resetRouteSheet.value){
        resetRouteSheet.value = false
        chooseRouteSheet = false
    }

    /*LaunchedEffect(bottomSheetState.bottomSheetState) {
        snapshotFlow { bottomSheetState.bottomSheetState.targetValue }.collect { isVisible ->
            if (isVisible == BottomSheetValue.Collapsed) {
                Log.d("Very good yes", isVisible.toString())
            } else {
                Log.d("Very good yes", isVisible.toString())
            }
        }
    }*/

        Log.d("Very Good!", bottomSheetState.bottomSheetState.currentValue.toString())

    Log.d("bottomSheet lala",bottomSheetState.bottomSheetState.currentValue.toString())

    BottomSheetScaffold(
        scaffoldState = bottomSheetState,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            if (!chooseRouteSheet) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    FloatingActionButton(onClick = {chooseRouteSheet = true}) {
                    }
                }
            } else {
                Image(painterResource(id = R.drawable.stack_icon), contentDescription = "Temp")
            }
        }) {
    }
}