package com.musify.app.presentation.myplaylist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.musify.app.R
import com.musify.app.domain.models.Playlist
import com.musify.app.ui.components.LoadingView
import com.musify.app.ui.components.LocalPlayListView
import com.musify.app.ui.components.NetworkErrorView
import com.musify.app.ui.theme.WhiteTextColor


@Composable
fun MyPlaylistsScreen(
    paddingValues: PaddingValues,
    myPlaylistsViewModel: MyPlaylistsViewModel,
    navigateToNewPlaylist: () -> Unit,
    navigateToLocalPlaylist: (Playlist) -> Unit,
) {

    val uiState by myPlaylistsViewModel.uiState.collectAsState()

    Scaffold(modifier = Modifier.padding(paddingValues = paddingValues), topBar = {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp, 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(id = R.string.my_playlists),
                fontSize = 22.sp,
                color = WhiteTextColor,
                fontWeight = FontWeight.Bold
            )
            IconButton(
                onClick = navigateToNewPlaylist,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.add),
                    contentDescription = "new playlist",
                    tint = WhiteTextColor
                )
            }
        }
    }) { padding ->

        when {
            uiState.isLoading -> {
                LoadingView(Modifier.fillMaxSize(1f))
            }

            uiState.isFailure -> {
                NetworkErrorView(Modifier.fillMaxSize()) {
                    myPlaylistsViewModel.getAllPlaylists()
                }
            }

            uiState.isSuccess -> {

                uiState.data?.let { data ->
                    LazyColumn(
                        modifier = Modifier.padding(padding),
                        contentPadding = PaddingValues(20.dp, 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {

                        items(data) { playlist ->
                            LocalPlayListView(playlist = playlist) {
                                navigateToLocalPlaylist(playlist)
                            }
                        }
                    }
                }

            }


        }
    }
}



