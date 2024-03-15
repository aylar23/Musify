package com.musify.app.ui.components.bottomsheet

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import coil.compose.rememberAsyncImagePainter
import com.musify.app.R
import com.musify.app.domain.models.Song
import com.musify.app.ui.components.ActionsModelView
import com.musify.app.ui.components.CustomButton
import com.musify.app.ui.theme.AlbumCoverBlackBG
import com.musify.app.ui.theme.GrayTextColor
import com.musify.app.ui.theme.SFFontFamily
import com.musify.app.ui.theme.SurfaceSecond
import com.musify.app.ui.theme.WhiteTextColor
import kotlin.math.absoluteValue


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun TrackBottomSheet(
    deletable: Boolean = false,
    selectedSong: Song,
    songSettingsSheetState: SheetState,
    onAddToPlaylist: () -> Unit,
    onPlayNext: () -> Unit,
    onNavigateToArtist: () -> Unit,
    onNavigateToAlbum: () -> Unit,
    onShare: () -> Unit,
    onDelete: () -> Unit = {},
    onDismiss: () -> Unit
) {


    ModalBottomSheet(
        modifier = Modifier,
        containerColor = AlbumCoverBlackBG,
        sheetState = songSettingsSheetState,
        dragHandle = {},
        onDismissRequest = onDismiss,
        ) {


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 20.dp)
        ) {


            Column(
                modifier = Modifier
                    .padding(top = 45.dp, start = 20.dp, end = 20.dp)
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(shape = MaterialTheme.shapes.large,
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
                        .padding(top = 5.dp),
                    text = selectedSong.getArtistsName(),
                    fontSize = 15.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontFamily = SFFontFamily,
                    fontWeight = FontWeight.Normal,
                    color = GrayTextColor
                )
            }

            ActionsModelView(
                expandable = true,
                icon = R.drawable.library_add,
                mainText = stringResource(id = R.string.add_to_playlist)
            ) {
                onDismiss()
                onAddToPlaylist()

            }


            ActionsModelView(
                expandable = false,
                icon = R.drawable.redo,
                mainText = stringResource(id = R.string.play_next)
            ) {
                onDismiss()
                onPlayNext()
            }


            ActionsModelView(
                expandable = true,
                icon = R.drawable.account_circle,
                mainText = stringResource(id = R.string.see_the_artist)
            ) {
                onDismiss()
                onNavigateToArtist()
            }


            if (selectedSong.albumId != null ){
                ActionsModelView(
                    expandable = true,
                    icon = R.drawable.album,
                    mainText = stringResource(id = R.string.see_the_album)
                ) {
                    onDismiss()
                    onNavigateToAlbum()
                }
            }

            if (deletable){
                ActionsModelView(
                    expandable = false,
                    icon = R.drawable.ic_trash,
                    mainText = stringResource(id = R.string.delete_from_playlist)
                ) {
                    onDelete()
                    onDismiss()

                }
            }

            ActionsModelView(
                expandable = false,
                icon = R.drawable.share,
                mainText = stringResource(id = R.string.share)
            ) {
                onDismiss()
                onShare()

            }

            CustomButton(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                text = R.string.cancel,
                onClick = onDismiss,
                containerColor = SurfaceSecond,
                contentColor = Color.White,
                shape = MaterialTheme.shapes.small
            )
        }

    }
}


