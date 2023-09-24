package com.musify.app.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import com.musify.app.presentation.common.SearchBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.musify.app.domain.models.mainScreenData
import com.musify.app.presentation.home.components.HomeTopAppBar
import com.musify.app.presentation.home.components.NewAlbumsView
import com.musify.app.presentation.home.components.TopArtistsView
import com.musify.app.presentation.home.components.TopPlaylistsView
import com.musify.app.presentation.home.components.TopSongsView


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    paddingValues: PaddingValues,
    homeViewModel: HomeViewModel,
    navigateToSearch: () -> Unit
) {

    Scaffold(
        modifier = Modifier.padding(paddingValues),
        topBar = {

        }
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


            TopPlaylistsView(mainScreenData.tops)

            TopArtistsView(mainScreenData.artists)


            NewAlbumsView(mainScreenData.playlists)


            TopSongsView(mainScreenData.hitSongs)


        }


    }
}