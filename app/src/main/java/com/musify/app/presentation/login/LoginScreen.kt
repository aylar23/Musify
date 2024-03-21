package com.musify.app.presentation.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.musify.app.R
import com.musify.app.Search
import com.musify.app.navigation.screen.LoginNavGraph
import com.musify.app.presentation.destinations.OTPScreenDestination
import com.musify.app.presentation.search.SearchViewModel
import com.musify.app.ui.components.CustomButton
import com.musify.app.ui.components.LoadingView
import com.musify.app.ui.components.clearFocusOnKeyboardDismiss
import com.musify.app.ui.theme.AlbumCoverBlackBG
import com.musify.app.ui.theme.GrayTextColor
import com.musify.app.ui.theme.Inactive
import com.musify.app.ui.theme.SFFontFamily
import com.musify.app.ui.theme.Surface
import com.musify.app.ui.theme.WhiteTextColor
import com.musify.app.ui.theme.Yellow
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch

@LoginNavGraph(start = true)
@Destination
@Composable
fun LoginScreen(
    navigator: DestinationsNavigator
) {

    val loginViewModel = hiltViewModel<LoginViewModel>()

    val uiState by loginViewModel.uiState.collectAsState()

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val focusRequester = remember { FocusRequester() }

    val phone by loginViewModel.phone.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(uiState.failure){
        if (uiState.failure){
            scope.launch {
                snackbarHostState.showSnackbar(
                    uiState.errorMessage
                )
            }

            loginViewModel.updateToDefault()
        }

    }

    LaunchedEffect(uiState.success){
        if (uiState.success){
            navigator.navigate(OTPScreenDestination("+993$phone"))
            loginViewModel.updateToDefault()

        }

    }
    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                Snackbar(
                    containerColor = Surface,
                    contentColor = Color.Red,
                    snackbarData = data
                )
            }
        },

        modifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                }
            },
    ) { padding ->

        Column(
            modifier = Modifier.padding(20.dp),

            ) {
            Text(
                text = stringResource(R.string.welcome),
                color = WhiteTextColor,
                style = TextStyle(
                    fontSize = 22.sp,
                    fontFamily = SFFontFamily,
                    fontWeight = FontWeight.Bold
                )
            )

            Text(
                text = stringResource(R.string.enter_phone_number_to_continue),
                color = GrayTextColor,
                style = TextStyle(
                    fontFamily = SFFontFamily,
                    fontSize = 15.sp
                ),
                modifier = Modifier
                    .padding(top = 10.dp)
                    .align(alignment = Alignment.Start)
            )

            Text(
                text = stringResource(R.string.phone_number),
                color = WhiteTextColor,
                lineHeight = 6.25.em,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = SFFontFamily,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .padding(top = 130.dp)
            )

            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
                    .clearFocusOnKeyboardDismiss()
                    .padding(top = 15.dp),
                value = phone,
                shape = MaterialTheme.shapes.medium,
                onValueChange = {
                    if(it.length <9 ){
                        loginViewModel.setPhone(it)
                    }
                },
                singleLine = true,
                trailingIcon = {

                    Icon(
                        painter = painterResource(id = R.drawable.ic_phone),
                        contentDescription = "song setting",
                        tint = GrayTextColor
                    )


                },
                leadingIcon = {

                    Row(
                        Modifier
                            .padding(start = 14.dp)
                            .height(IntrinsicSize.Min)

                    ) {
                        Text(
                            text = "+993",
                            style = TextStyle(
                                fontSize = 15.sp,
                                lineHeight = 15.sp,
                                fontFamily = SFFontFamily,
                                fontWeight = FontWeight.Normal,
                                color = GrayTextColor,
                            )
                        )
                        VerticalDivider(
                            color = GrayTextColor,
                            modifier = Modifier
                                .padding(horizontal = 10.dp)
                        )
                    }
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
                    imeAction = ImeAction.Default, keyboardType = KeyboardType.Number

                ),
                maxLines = 1,
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                        loginViewModel.loginUser(phone.trim())
                    },
                ),
            )

            CustomButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                text = R.string.continue_string,
                onClick = {
                    loginViewModel.loginUser(phone.trim())
                },
                containerColor = Yellow,
                contentColor = AlbumCoverBlackBG,
                shape = MaterialTheme.shapes.small

            )
        }

        if (uiState.loading) {

            Dialog(
                onDismissRequest = { },
            ) {
                LoadingView(
                    Modifier.fillMaxSize()
                )
            }


        }
    }
}