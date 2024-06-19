package com.example.capstonemad

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.material3.SearchBar
import androidx.compose.runtime.mutableStateListOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(onSearch: (String) -> Unit) {

    var text by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    var items = remember {
        mutableStateListOf(
            "Den Haag",
            "Leiden"
        )
    }

    SearchBar(
        modifier = Modifier.fillMaxWidth(),
        query = text,
        onQueryChange = {
            text = it
        },
        onSearch = {
            if (text != "" && !items.contains(text)) {
                items.add(text)
            }
            active = false
            onSearch(text)
        },
        active = active,
        onActiveChange = {
            active = it
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
        placeholder = {
            Text(text = "Search")
        },
        trailingIcon = {
            if (active) {
                Icon(

                    modifier = Modifier.clickable {
                        if (text.isNotEmpty()) {
                            text = ""
                        } else {
                            active = false
                        }
                    },
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close icon"
                )
            }
        }
    ) {
        items.forEach() {
            Row(modifier = Modifier.padding(all = 14.dp)) {
                Box(modifier = Modifier
                    .clickable {
                        text = it
                        active = false
                        onSearch(text)
                    }
                    .fillMaxWidth()) {
                    Icon(
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .align(Alignment.CenterStart),
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "History icon"
                    )
                    Text(
                        modifier = Modifier
                            .padding(start = 35.dp)
                            .align(Alignment.CenterStart),
                        text = it
                    )
                }
            }
        }
    }
}