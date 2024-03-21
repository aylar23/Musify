package com.musify.app.presentation.otp

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.musify.app.R
import com.musify.app.navigation.screen.LoginNavGraph
import com.musify.app.presentation.destinations.HomeScreenDestination
import com.musify.app.presentation.destinations.OTPScreenDestination
import com.musify.app.presentation.login.LoginViewModel
import com.musify.app.ui.components.CustomButton
import com.musify.app.ui.components.LoadingView
import com.musify.app.ui.components.clearFocusOnKeyboardDismiss
import com.musify.app.ui.theme.AlbumCoverBlackBG
import com.musify.app.ui.theme.GrayTextColor
import com.musify.app.ui.theme.SFFontFamily
import com.musify.app.ui.theme.Surface
import com.musify.app.ui.theme.WhiteTextColor
import com.musify.app.ui.theme.Yellow
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch


@LoginNavGraph
@Destination
@Composable
fun OTPScreen(
    phone:String,
    navigator: DestinationsNavigator
) {
    val verificationViewModel = hiltViewModel<VerificationViewModel>()

    val uiState by verificationViewModel.uiState.collectAsState()

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val focusRequester = remember { FocusRequester() }

    val (code, onCodeChange) = remember {
        mutableStateOf("")
    }
    val (isFocused, onFocusChange) = remember {
        mutableStateOf(false)
    }
    val (isError, onErrorChange) = remember {
        mutableStateOf(false)
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(uiState.failure){
        if (uiState.failure){
            scope.launch {
                snackbarHostState.showSnackbar(
                    uiState.errorMessage
                )
            }

            verificationViewModel.updateToDefault()
        }

    }

    LaunchedEffect(uiState.success){

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
                text = stringResource(R.string.enter_verification_code),
                color = WhiteTextColor,
                style = TextStyle(
                    fontSize = 22.sp,
                    fontFamily = SFFontFamily,
                    fontWeight = FontWeight.Bold
                )
            )

            Text(
                text = stringResource(R.string.enter_verification_code_desc),
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
                style = TextStyle(
                    fontFamily = SFFontFamily,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .padding(top = 130.dp)
            )

            BasicTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp)
                    .onFocusChanged {
                        onFocusChange(it.isFocused)
                    },
                value = code,
                onValueChange = {
                    if (it.length > 6) return@BasicTextField
                    onCodeChange(it)
                    if (it.length == 6) verificationViewModel.verify(phone, it)

                },
                decorationBox = {
                    Row(horizontalArrangement = Arrangement.Center) {
                        repeat(6) { index ->
                            CharView(
                                isTextFieldFocused = isFocused,
                                isError = isError,
                                index = index,
                                text = code
                            )
                            Spacer(modifier = Modifier.width(9.dp))
                        }
                    }
                },

                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done, keyboardType = KeyboardType.Number
                ),
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                    keyboardController?.hide()

                }),
            )

            if (isError) {
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    text = stringResource(R.string.invalid_code_desc),
                    style = TextStyle(
                        fontSize = 14.sp,
                        lineHeight = 16.8.sp,
                        fontFamily = SFFontFamily,
                        fontWeight = FontWeight(400),
                        color = Red,

                        textAlign = TextAlign.Center,
                    )
                )

            }

//            CustomButton(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(top = 20.dp),
//                text = R.string.continue_string,
//                onClick = {
//                    loginViewModel.loginUser(phone.trim())
//                },
//                containerColor = Yellow,
//                contentColor = AlbumCoverBlackBG,
//                shape = MaterialTheme.shapes.small
//
//            )
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