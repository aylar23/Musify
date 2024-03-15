package com.musify.app.presentation.albums

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.musify.app.R
import com.musify.app.ui.components.AlbumView
import com.musify.app.ui.components.CollapsingSmallTopAppBar
import com.musify.app.ui.components.LoadingView
import com.musify.app.ui.components.NetworkErrorView
import com.musify.app.ui.components.NotFoundView
import com.musify.app.ui.theme.AlbumCoverBlackBG


@Composable
fun AlbumsScreen(
    artistId: Long,
    paddingValues: PaddingValues,
    albumsViewModel: AlbumsViewModel,
    navigateUp: () -> Unit
) {

    LaunchedEffect(artistId) {
        albumsViewModel.setArtistId(artistId)
    }

    val albums = albumsViewModel.albums.collectAsLazyPagingItems()

    Scaffold(modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .background(
            AlbumCoverBlackBG
        ),
        topBar = {

            CollapsingSmallTopAppBar(
                title = stringResource(id = R.string.albums)
            ) {
                navigateUp()
            }

        }
    ) { padding ->

        if (albums.itemCount == 0 && albums.loadState.refresh == LoadState.Loading) {
            LoadingView(Modifier.fillMaxSize())
        } else if (albums.itemCount == 0 && albums.loadState.refresh is LoadState.Error) {
            NetworkErrorView(Modifier.fillMaxSize()) {
                albums.retry()
                albums.refresh()
            }
        } else if (albums.itemCount == 0) {
            NotFoundView(Modifier.fillMaxSize())
        } else {
            LazyVerticalGrid(
                modifier =Modifier.padding(top = padding.calculateTopPadding()),
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(20.dp)
            ) {
                items(albums.itemCount) { index ->
                    val album = albums[index]!!

                    AlbumView(album = album, isGrid = true) { }

                }


            }
        }
    }
}