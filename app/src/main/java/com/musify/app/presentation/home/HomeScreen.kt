package com.musify.app.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import com.musify.app.ui.components.SearchBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.musify.app.R
import com.musify.app.domain.models.mainScreenData
import com.musify.app.ui.components.listview.AlbumListView
import com.musify.app.ui.components.listview.ArtistListView
import com.musify.app.ui.components.listview.PlaylistListView
import com.musify.app.ui.components.listview.SongGridListView
import com.musify.app.presentation.home.components.HomeTopAppBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    paddingValues: PaddingValues,
    homeViewModel: HomeViewModel,
    navigateToSearch: () -> Unit,
    navigateToArtist: () -> Unit
) {

    Scaffold(
        modifier = Modifier.padding(paddingValues),
    ) { padding ->



        Column(
            Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            HomeTopAppBar()
            SearchBar(
                enabled = false,
                onClick = navigateToSearch,
            ) {}


            PlaylistListView(mainScreenData.tops)

            ArtistListView(
                header =  R.string.top,
                mainScreenData.artists
            ){navigateToArtist()}


            AlbumListView(mainScreenData.albums){

            }


            SongGridListView(
                mainScreenData.hitSongs,
                onMoreClicked = {

                }
            ){}


        }


    }
}