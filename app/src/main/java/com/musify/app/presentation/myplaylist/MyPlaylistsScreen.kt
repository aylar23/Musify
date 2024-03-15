package com.musify.app.presentation.myplaylist

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.musify.app.R
import com.musify.app.domain.models.Playlist
import com.musify.app.domain.models.Playlist.Companion.ALBUM
import com.musify.app.domain.models.Playlist.Companion.ALL
import com.musify.app.domain.models.Playlist.Companion.ALL_SEARCH
import com.musify.app.domain.models.Playlist.Companion.ARTIST
import com.musify.app.domain.models.Playlist.Companion.PLAYLIST
import com.musify.app.domain.models.Playlist.Companion.SONG
import com.musify.app.presentation.player.NewPlaylistDialog
import com.musify.app.presentation.search.SearchViewModel
import com.musify.app.ui.components.LoadingView
import com.musify.app.ui.components.LocalPlayListView
import com.musify.app.ui.components.NetworkErrorView
import com.musify.app.ui.theme.Inactive
import com.musify.app.ui.theme.SFFontFamily
import com.musify.app.ui.theme.Surface
import com.musify.app.ui.theme.WhiteTextColor
import com.musify.app.ui.theme.Yellow
import kotlinx.coroutines.launch


@Composable
fun MyPlaylistsScreen(
    paddingValues: PaddingValues,
    myPlaylistsViewModel: MyPlaylistsViewModel,
    navigateToNewPlaylist: () -> Unit,
    navigateToLocalPlaylist: (Playlist) -> Unit,
) {

    var showNewPlaylistDialog by remember {
        mutableStateOf(false)
    }

    var selectedPlaylist by remember {
        mutableStateOf<Playlist?>(null)
    }
    val playlists by myPlaylistsViewModel.playlists.collectAsState(initial = emptyList())
    val type by myPlaylistsViewModel.type.collectAsState("")
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val snackbarAddMessage = stringResource(id = R.string.successfully_added)
    val snackbarDeleteMessage = stringResource(id = R.string.successfully_deleted)
    val sections = listOf(
        LibrarySection.All,
        LibrarySection.Playlist,
        LibrarySection.Album
    )
    Scaffold(
        modifier = Modifier.padding(paddingValues = paddingValues),
        topBar = {

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
                    onClick = { showNewPlaylistDialog = true },
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.library_add),
                        contentDescription = "new playlist",
                        tint = WhiteTextColor
                    )
                }
            }
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                Snackbar(
                    containerColor = Inactive,
                    contentColor = WhiteTextColor,
                    snackbarData = data
                )
            }
        },
    ) { padding ->


        Column(modifier = Modifier.padding(top = padding.calculateTopPadding())) {
            LibraryContentChipsView(
                sections = sections,
                selected = type) { type ->
                myPlaylistsViewModel.setType(type)
            }
            LazyColumn(

                contentPadding = PaddingValues(20.dp, 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                items(playlists) { playlist ->
                    LocalPlayListView(playlist = playlist, count = playlist.songsCount, onEdit = {
                        showNewPlaylistDialog = true
                        selectedPlaylist = playlist
                    }, onDelete = {
                        myPlaylistsViewModel.deletePlaylist(playlist)
                        scope.launch {
                            snackbarHostState.showSnackbar(snackbarDeleteMessage)
                        }
                    }, selectPlaylist = {
                        navigateToLocalPlaylist(playlist)
                    })


                }
            }
        }





        if (showNewPlaylistDialog) {
            Dialog(onDismissRequest = {
                showNewPlaylistDialog = false
                selectedPlaylist = null
            }) {
                NewPlaylistDialog(
                    playlist = selectedPlaylist,
                    onEdit = { playlist ->
                        showNewPlaylistDialog = false
                        myPlaylistsViewModel.updatePlaylist(playlist)
                        selectedPlaylist = null
                    },

                    ) { name ->
                    showNewPlaylistDialog = false
                    myPlaylistsViewModel.addNewPlaylist(name)
                    scope.launch {
                        snackbarHostState.showSnackbar(snackbarAddMessage)
                    }


                }
            }

        }
    }


}

@Composable
fun LibraryContentChipsView(
    sections: List<LibrarySection>,
    selected: String,
    onClick: (String) -> Unit
) {



    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp)
    ) {

        items(sections) { section ->
            val containerColor = if (selected == section.value) Yellow else Surface
            val contentColor =
                if (selected == section.value) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.onBackground


            Text(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.extraSmall)
                    .background(containerColor)
                    .clickable { onClick(section.value) }
                    .padding(20.dp, 10.dp),
                text = stringResource(id = section.title),
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight(500),
                    fontFamily = SFFontFamily,
                    color = contentColor

                )

            )
        }

    }
}

sealed class LibrarySection(
    @StringRes val title: Int,
    val value: String,
) {
    object All : LibrarySection(
        R.string.all, ALL
    )

    object Playlist : LibrarySection(
        R.string.playlist, PLAYLIST
    )

    object Album : LibrarySection(
        R.string.album, ALBUM
    )

    object AllSearch : LibrarySection(
        R.string.all, ALL_SEARCH
    )

    object Song : LibrarySection(
        R.string.song, SONG
    )

    object Artist : LibrarySection(
        R.string.artist, ARTIST
    )
}
