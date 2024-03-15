package com.musify.app.ui.components.bottomsheet

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.musify.app.R
import com.musify.app.domain.models.Artist
import com.musify.app.domain.models.Song
import com.musify.app.ui.components.ActionsModelView
import com.musify.app.ui.components.CustomButton
import com.musify.app.ui.components.LocalPlaylistSelectionView
import com.musify.app.ui.theme.AlbumCoverBlackBG
import com.musify.app.ui.theme.DarkGray
import com.musify.app.ui.theme.GrayTextColor
import com.musify.app.ui.theme.SFFontFamily
import com.musify.app.ui.theme.SurfaceSecond
import com.musify.app.ui.theme.TransparentColor
import com.musify.app.ui.theme.WhiteTextColor
import com.musify.app.ui.theme.Yellow


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ArtistBottomSheet(
    selectedSong: Song,
    artists: List<Artist>,
    sheetState: SheetState,
    onSelect: (Artist) -> Unit,
    onDismiss: () -> Unit
) {


    ModalBottomSheet(
        containerColor = AlbumCoverBlackBG,
        sheetState = sheetState,
        onDismissRequest = onDismiss,
        dragHandle = {}
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
            ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(top = 45.dp, start = 20.dp, end = 20.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    shape = MaterialTheme.shapes.large,
                    modifier = Modifier
                        .width(210.dp)
                        .aspectRatio(1f)
                ) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        painter = rememberAsyncImagePainter(
                            model = selectedSong.getSongImage() ?: ""
                        ),
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                    )
                }
                Text(
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .basicMarquee(
                            iterations = Int.MAX_VALUE,
                        ),
                    text = selectedSong.name,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontFamily = SFFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = WhiteTextColor,
                )
                Text(
                    modifier = Modifier
                        .padding(top = 5.dp, bottom = 10.dp),
                    text = selectedSong.getArtistsName(),
                    fontSize = 15.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontFamily = SFFontFamily,
                    fontWeight = FontWeight.Normal,
                    color = GrayTextColor
                )

                Spacer(modifier = Modifier.weight(1f))

                artists.forEach { artist ->

                    ActionsModelView(
                        expandable = true,
                        icon = R.drawable.account_circle,
                        mainText = artist.name
                    ) {
                        onDismiss()
                        onSelect(artist)
                    }


                }

                Spacer(modifier = Modifier.height(20.dp))
            }


            CustomButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                text = R.string.cancel,
                onClick = onDismiss,
                containerColor = SurfaceSecond,
                contentColor = Color.White,
                shape = MaterialTheme.shapes.small
            )
        }


    }

}
