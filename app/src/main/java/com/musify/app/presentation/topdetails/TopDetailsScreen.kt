package com.musify.app.presentation.topdetails

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.musify.app.R
import com.musify.app.domain.models.Song
import com.musify.app.domain.models.defaultSong
import com.musify.app.presentation.common.SongView
import com.musify.app.ui.theme.AlbumCoverBlackBG
import com.musify.app.ui.theme.DarkGray
import com.musify.app.ui.theme.SFFontFamily
import com.musify.app.ui.theme.TransparentColor
import com.musify.app.ui.theme.WhiteTextColor
import com.musify.app.ui.theme.Yellow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopDetailsScreen(paddingValues: PaddingValues, scrollBehavior: TopAppBarScrollBehavior){
    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .nestedScroll(scrollBehavior.nestedScrollConnection)) {
        item {
            Column(modifier = Modifier.background(
                brush = Brush.verticalGradient(
                    startY = 0f,
                    endY = 240f,
                    colors = listOf(
                        TransparentColor,
                        AlbumCoverBlackBG
                    )
                )
            )){
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 60.dp)
                    .padding(vertical = 4.dp, horizontal = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = "TNT Music App", color = WhiteTextColor, fontFamily = SFFontFamily, fontWeight = FontWeight.Bold)
                    OutlinedIconButton(
                        onClick = { /*TODO*/ },
                        shape = RoundedCornerShape(50.dp),
                        border = BorderStroke(
                            width = 1.dp,
                            color = WhiteTextColor
                        )
                    ) {
                        Icon(painter = painterResource(id = R.drawable.group), tint = WhiteTextColor, contentDescription = "")
                    }
                }
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp, horizontal = 10.dp),
                    horizontalArrangement = Arrangement.Center){
                    TextButton(
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(10.dp))
                            .background(color = Yellow)
                            .weight(1f),
//                            .fillMaxWidth(.6f),
                        onClick = { /*TODO*/ }) {
                        Text(text = "Hemmesini cal", color = WhiteTextColor, fontWeight = FontWeight.SemiBold, fontFamily = SFFontFamily)
                    }
                    Spacer(modifier = Modifier.weight(.1f))
                    TextButton(
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(10.dp))
                            .background(color = DarkGray)
                            .weight(1f),
                        onClick = { /*TODO*/ }) {
                        Icon(painter = painterResource(id = R.drawable.play), tint = WhiteTextColor,
                            contentDescription = "Shuffle")
                        Text(text = "Shuffle", color = WhiteTextColor, fontWeight = FontWeight.SemiBold, fontFamily = SFFontFamily)
                    }

                }
            }

        }

        songViewsList()

    }


}

/**
 * Gives us the ability to use dynamic and static views inside lazycolumn.
 */
fun LazyListScope.songViewsList(){
        items(mockSongs().size){id ->
            SongView(song = mockSongs()[id])
        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(scrollBehaviour: TopAppBarScrollBehavior){
    LargeTopAppBar(
        title = {
            Text(
                text = if (scrollBehaviour.state.collapsedFraction == 1f){"TNT"} else {""},
                color = WhiteTextColor,
                fontSize = 16.sp,
                fontFamily = SFFontFamily,
                fontWeight = FontWeight(700)
            ) },
        scrollBehavior = scrollBehaviour,
        navigationIcon = {

            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    painter = painterResource(id = R.drawable.left_arrow),
                    contentDescription = "profile button",
                    tint = WhiteTextColor,
                    modifier = Modifier
                        .size(24.dp)

                )
            }
        },
        colors = if (scrollBehaviour.state.collapsedFraction == 1f){
            TopAppBarDefaults.topAppBarColors(containerColor = AlbumCoverBlackBG)
        } else {
            TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)},
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
private fun TopDetailsBgImage(){
    val appBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(appBarState)
    Box (modifier = Modifier
        .width(480.dp)
        .height(800.dp)){

    }

    Scaffold(
        topBar = { TopBar(scrollBehaviour = scrollBehavior)}
    ) { paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .background(AlbumCoverBlackBG)){
            Image(
                painter = painterResource(id = R.drawable.mock_cover),
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp))
        }
        TopDetailsScreen(paddingValues, scrollBehavior)
    }

}


fun mockSongs():MutableList<Song>{
    val mockSongsList: MutableList<Song> = mutableListOf()
    for (i in 1..20){
        mockSongsList.add(defaultSong)
    }
    return mockSongsList
}
