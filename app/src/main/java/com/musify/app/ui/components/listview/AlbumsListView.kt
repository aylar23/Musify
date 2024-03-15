package com.musify.app.ui.components.listview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.musify.app.R
import com.musify.app.domain.models.Playlist
import com.musify.app.ui.components.AlbumView
import com.musify.app.ui.components.HeaderView
import com.musify.app.ui.theme.Background
import com.musify.app.ui.theme.SFFontFamily


@Composable
fun AlbumListView(
    title: String = stringResource(id = R.string.albums),
    playlists: List<Playlist>,
    expandable:Boolean = false,
    navigateToAlbums: () -> Unit = {},
    onClick: (Playlist) -> Unit,
) {
    if (playlists.isNotEmpty()) {
        Column(
            modifier = Modifier.background(Background)
        ) {

            HeaderView(
                modifier = Modifier.padding(top = 10.dp),
                mainText = title,
                expandable = expandable
            ){
                navigateToAlbums()
            }

            LazyRow(
                contentPadding = PaddingValues(20.dp, 15.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(playlists) { playlist ->
                    AlbumView(playlist) { onClick(playlist) }
                }
            }
        }


    }
}
