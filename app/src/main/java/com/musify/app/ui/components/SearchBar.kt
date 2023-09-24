package com.musify.app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.musify.app.R
import com.musify.app.ui.theme.GrayTextColor
import com.musify.app.ui.theme.SFFontFamily
import com.musify.app.ui.theme.Surface
import com.musify.app.ui.theme.Yellow


@Composable
fun SearchBar(
    searchStr :MutableState<String> = mutableStateOf(""),
    enabled: Boolean,
    onClick: () -> Unit,
    onDone: () -> Unit
) {


    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                if (!enabled) onClick()
            }
            .padding(horizontal = 20.dp),
        value = searchStr.value,
        shape = MaterialTheme.shapes.medium,
        onValueChange = {
            searchStr.value = it
        },
        enabled = enabled,
        singleLine = true,
        trailingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.search),
                contentDescription = "song setting",
                tint = GrayTextColor
            )
        },
        placeholder = {
            Text(
                text = stringResource(id = R.string.search),
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
        keyboardActions = KeyboardActions(onDone = { onDone() }),
    )
}