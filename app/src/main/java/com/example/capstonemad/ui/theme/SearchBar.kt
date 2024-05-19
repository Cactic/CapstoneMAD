package com.example.capstonemad.ui.theme

import android.view.View.OnClickListener
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.capstonemad.R
import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.material3.SearchBar
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.draw.alpha

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(){
    val searchQueryState = rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }

    var text by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    var items = remember {
        mutableStateListOf(
            "Den Haag",
            "Leiden")
    }

    SearchBar(
        modifier = Modifier.fillMaxWidth(),
        query = text,
        onQueryChange = {
            text = it
        },
        onSearch = {
            if(text != "" && !items.contains(text)){
                items.add(text)
            }
            active = false
        },
        active = active
        ,
        onActiveChange = {
            active  = it
        },
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = "",
                modifier = Modifier
                    .padding(16.dp)
                    .size(24.dp)
            )
        },
        placeholder ={
            Text(text = "Searchs")
        },
        trailingIcon = {
            if(active){
                Icon(

                    modifier = Modifier.clickable {
                        if(text.isNotEmpty()){
                            text = ""
                        }else {
                            active = false
                        }
                    },
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close icon"
                )
            }
        }
    ) {
        items.forEach(){
            Row(modifier = Modifier.padding(all = 14.dp)){
                Box(modifier = Modifier
                    .clickable { text = it }
                    .fillMaxWidth()){
                    Icon(
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .align(Alignment.CenterStart),
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Gistory icon")
                    Text(modifier = Modifier
                        .padding(start = 35.dp)
                        .align(Alignment.CenterStart),
                        text = it)

                }
            }
        }
    }

    /*Box( Modifier
        .fillMaxWidth()){
        TextField(
            value = searchQueryState.value,
            onValueChange = {value ->
                searchQueryState.value = value
            },
            Modifier
                .fillMaxWidth(0.9f)
                .padding(vertical = 40.dp)
                .align(Alignment.TopCenter)
                .clickable { searching = true
                           Log.d("clicking test", searching.toString())},
            textStyle = TextStyle(fontSize = 18.sp),
            leadingIcon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "",
                    modifier = Modifier
                        .padding(16.dp)
                        .size(24.dp)
                )
            },
            placeholder = {
                Text(text = stringResource(id = R.string.searchText))
            },
            singleLine = true,
            shape = CircleShape,

            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }*/
}