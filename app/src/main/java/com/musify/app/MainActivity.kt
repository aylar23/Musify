package com.musify.app

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.musify.app.navigation.AppNavGraph
import com.musify.app.ui.theme.MusifyTheme
import com.skydoves.flexible.core.FlexibleSheetSize
import com.skydoves.flexible.core.FlexibleSheetValue
import com.skydoves.flexible.core.rememberFlexibleBottomSheetState
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("StaticFieldLeak")
var currentInnerNavController: NavHostController? = null

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            MusifyTheme {
                // A surface container using the 'background' color from the theme
                val mainViewModel = hiltViewModel<MainViewModel>()
                val playerBottomSheet = rememberFlexibleBottomSheetState(
                    skipIntermediatelyExpanded = true,
                    skipSlightlyExpanded = true,
                    containSystemBars = false,
                    allowNestedScroll = true,
                    isModal = true,
                    flexibleSheetSize = FlexibleSheetSize(1f, 0.5f, 0.001f)

                )


                val scope = rememberCoroutineScope()

                Surface {
                    AppNavGraph(
                        mainViewModel = mainViewModel,
                        playerBottomSheet = playerBottomSheet
                    )

                }


            }

        }
    }


    companion object {

        val TOPS = "tops"
        val PLAYLISTS = "playlists"
        val ALBUMS = "albums"
    }


}






