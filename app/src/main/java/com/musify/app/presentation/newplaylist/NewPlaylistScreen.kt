package com.musify.app.presentation.newplaylist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.musify.app.ui.components.CustomButton
import com.musify.app.ui.theme.AlbumCoverBlackBG
import com.musify.app.ui.theme.GrayTextColor
import com.musify.app.ui.theme.SFFontFamily
import com.musify.app.ui.theme.Surface
import com.musify.app.ui.theme.WhiteTextColor
import com.musify.app.ui.theme.Yellow


@Preview
@Composable
fun NewPlaylistScreen() {
    var nameStr by rememberSaveable {
        mutableStateOf("")
    }
    var enabled by rememberSaveable {
        mutableStateOf(true)
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current


    Column(
        modifier = Modifier.fillMaxSize().background(AlbumCoverBlackBG),
        verticalArrangement = Arrangement.SpaceBetween
        ) {
        Column (modifier = Modifier.fillMaxWidth()){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp, 10.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = stringResource(id = R.string.playlist_name),
                    fontSize = 22.sp,
                    color = WhiteTextColor,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp, 10.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            if (!enabled) focusManager.clearFocus()
                        },
                    value = nameStr,
                    shape = MaterialTheme.shapes.small,
                    onValueChange = {
                        nameStr = it
                    },
                    enabled = enabled,
                    singleLine = true,
                    textStyle = TextStyle(
                        fontSize = 15.sp,
                        lineHeight = 15.sp,
                        fontFamily = SFFontFamily,
                        fontWeight = FontWeight.Normal,
                        color = WhiteTextColor,
                    ),

                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.name),
                            style = TextStyle(
                                fontSize = 15.sp,
                                lineHeight = 15.sp,
                                fontFamily = SFFontFamily,
                                fontWeight = FontWeight.Normal,
                                color = GrayTextColor,
                            )
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Surface,
                        focusedContainerColor = Surface,
                        disabledContainerColor = Surface,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        cursorColor = Yellow
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Default, keyboardType = KeyboardType.Password

                    ),
                    maxLines = 1,
                    keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
                )
            }
        }
        Box (modifier = Modifier.padding(20.dp)){
            CustomButton(
                modifier = Modifier.fillMaxWidth(),
                text = R.string.save,
                onClick = { /*TODO*/ },
                containerColor = Yellow,
                contentColor = AlbumCoverBlackBG,
                shape = MaterialTheme.shapes.small
            )
        }
    }



}