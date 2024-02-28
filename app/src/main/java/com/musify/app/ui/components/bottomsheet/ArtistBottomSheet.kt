package com.musify.app.ui.components.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.musify.app.R
import com.musify.app.domain.models.Artist
import com.musify.app.ui.components.ActionsModelView
import com.musify.app.ui.components.CustomButton
import com.musify.app.ui.theme.AlbumCoverBlackBG
import com.musify.app.ui.theme.SurfaceSecond
import com.musify.app.ui.theme.TransparentColor


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtistBottomSheet(
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
                .fillMaxWidth()
                .padding(20.dp),

            ) {
            LazyColumn(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.large)

            ) {


                items(artists) { artist ->

                    ActionsModelView(
                        expandable = true,
                        icon = R.drawable.account_circle,
                        mainText = artist.name
                    ) {
                        onDismiss()
                        onSelect(artist)
                    }

                }


            }

            Spacer(
                modifier = Modifier
                    .height(20.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            startY = 0f, endY = 250f, colors = listOf(
                                TransparentColor, AlbumCoverBlackBG
                            )
                        )
                    )
            )

            CustomButton(
                modifier = Modifier.fillMaxWidth().padding(20.dp),
                text = R.string.cancel,
                onClick =  onDismiss,
                containerColor = SurfaceSecond,
                contentColor = Color.White,
                shape = MaterialTheme.shapes.small
            )
        }


    }
}