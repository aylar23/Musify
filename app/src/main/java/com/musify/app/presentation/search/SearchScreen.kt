package com.musify.app.presentation.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.musify.app.R
import com.musify.app.domain.models.Artist
import com.musify.app.domain.models.mainScreenData
import com.musify.app.ui.components.SearchBar
import com.musify.app.ui.components.listview.ArtistListView
import com.musify.app.ui.components.listview.SongListView
import com.musify.app.presentation.search.components.SearchKeysView

@Composable
fun SearchScreen(
    paddingValues: PaddingValues,
    searchViewModel: SearchViewModel,
    navigateToArtist: (Artist) -> Unit

) {


    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val searchStr = rememberSaveable {
        mutableStateOf("")
    }

    val keys = remember {
        mutableStateListOf("wer", "wer1", "wer2", "wer3", "wer4")
    }
    Scaffold(
        modifier = Modifier.padding(paddingValues)
    ) { padding ->


        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(modifier = Modifier.height(1.dp))

            SearchBar(
                searchStr = searchStr,
                enabled = true,
                onClick = {},
            ) {
                focusManager.clearFocus()
                keyboardController?.hide()
            }

            if (searchStr.value.isEmpty()) {
                SearchKeysView(
                    keys = keys,
                    onDelete = { keys.remove(it) },
                    onSearch = { searchStr.value = it }
                )
            } else {

                ArtistListView(
                    header = R.string.artists,
                    mainScreenData.artists
                ) { artist -> navigateToArtist(artist) }



                SongListView(
                    mainScreenData.hitSongs,
                    onMoreClicked = {

                    }
                ) {

                }

            }


        }


    }
}

