package com.musify.app.presentation.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.musify.app.R
import com.musify.app.domain.models.Playlist
import com.musify.app.ui.theme.AlbumCoverBlackBG
import com.musify.app.ui.theme.Black
import com.musify.app.ui.theme.SFFontFamily
import com.musify.app.ui.theme.Surface
import com.musify.app.ui.theme.WhiteTextColor
import com.musify.app.ui.theme.Yellow


@Composable
fun NewPlaylistDialog(
    playlist: Playlist? = null,
    onEdit: (Playlist) -> Unit = {},
    onAdd: (String) -> Unit,

) {

    var playlistName by remember { mutableStateOf(playlist?.name ?: "") }
    val keyboardController = LocalSoftwareKeyboardController.current

    val focusManager = LocalFocusManager.current


    Column(
        modifier = Modifier
            .clip(MaterialTheme.shapes.medium)
            .background(AlbumCoverBlackBG)
            .padding(20.dp)
    ) {
        Text(
            text = stringResource(R.string.new_playlist),

            style = TextStyle(
                fontSize = 22.sp,
                lineHeight = 22.sp,
                fontWeight = FontWeight(700),
                color = Color(0xFFD3D3D3),

                )
        )
        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = playlistName,
            onValueChange = {
                if (it.length <= 8) {
                    playlistName = it
                }
            },

            maxLines = 1,
            singleLine = true,
            textStyle = TextStyle(
                fontSize = 14.sp,
                lineHeight = 18.sp,
                fontFamily = SFFontFamily,
                fontWeight = FontWeight(400),
                color = WhiteTextColor,
            ),
            placeholder = {
                Text(
                    text = stringResource(id = R.string.name),
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 16.sp,
                        fontFamily = SFFontFamily,
                        fontWeight = FontWeight(700),
                        color = Color(0xFF333333),
                    )

                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = WhiteTextColor,
                unfocusedTextColor = WhiteTextColor,
                unfocusedContainerColor = Surface,
                focusedContainerColor = Surface,
                focusedBorderColor = Surface,
                unfocusedBorderColor = Surface,
                disabledBorderColor = Surface,

                ),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text
            ),
            shape = MaterialTheme.shapes.small,
            keyboardActions = KeyboardActions(onDone = {
                focusManager.clearFocus()
                keyboardController?.hide()

            }),
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                if (playlist == null){
                    onAdd(playlistName)
                }else{
                    onEdit(playlist.copy(name = playlistName))
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                disabledContainerColor = Yellow.copy(alpha = 0.5f),
                contentColor = MaterialTheme.colorScheme.background
            ),
            enabled = playlistName.isNotEmpty(),
            shape = MaterialTheme.shapes.small,
            contentPadding = PaddingValues(vertical = 14.dp, horizontal = 40.dp)
        ) {
            Text(
                text = stringResource(id = R.string.save),
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 16.sp,
                    fontFamily = SFFontFamily,
                    fontWeight = FontWeight(700),
                    color = MaterialTheme.colorScheme.background,
                )

            )

        }
    }


}



