package com.example.hltv.ui.screens.teamsScreen
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.compose.runtime.collectAsState
import coil.compose.rememberAsyncImagePainter

//Used for testing
val items = (1..20).map { index ->
    ListItem(index, "Item $index Text 1", "Item $index Text 2")
}
data class ListItem(val ranking: Int, val text1: String, val text2: String)

@Composable
fun RankingScreen() {
    val R = MaterialTheme
    val viewModel = RankingScreenViewModel()
    val allPlayerImages = viewModel.allPlayerImages.collectAsState()
    val playerbmap = viewModel.playerImage.collectAsState()

    LazyColumn {

        items(viewModel.teamNames.size) { index ->

            teamCard(modifier = Modifier, R = R, text1 = viewModel.teamNames[index], text2 = "Unused", playerbmap, allPlayerImages) //ugly hardcoding, but we ball
            if (index < items.size-1){
                Spacer(modifier = Modifier.height(1.dp))
            }
        }
    }
}

@Composable
fun teamCard(
    modifier: Modifier,
    R: MaterialTheme,
    text1: String = " ",
    text2: String,
    singleImgState: State<img>,
    allPlayerImages: State<AllPlayerImages>
    ) =

    Card (
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(131.dp)
    ){
        Box(modifier = Modifier
            .background(color = Color.DarkGray)
            .fillMaxWidth()
            .height(42.dp)
        ) {
            Text(
                text = text1,
                fontSize = R.typography.bodyLarge.fontSize,
                color = R.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 10.dp)
            )

        }
        Row (modifier = Modifier.padding(start = 10.dp)) {

            for (i in 1..5){
                Column (modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)){
                    Log.i("teamCard", "allPlayerImages is being rendered: " + allPlayerImages.value.toString())
                    Image(
                        //painter = painterResource(id = com.example.hltv.R.drawable.astralis_logo),
                        /*allPlayerImagesState.value.allTeamImages?.get(0)?.teamImages?.get(0)*/

                        painter = rememberAsyncImagePainter(allPlayerImages.value.allTeamImages?.get(0)?.teamImages?.get(0)),
                        contentDescription = null, //TODO
                        alignment = Alignment.TopStart,
                        modifier = Modifier
                            .size(69.dp)
                            .offset(y = 10.dp)
                    )
                    Card (
                        modifier = modifier
                            .fillMaxWidth()
                            .height(12.dp)
                            .padding(end = 10.dp)
                    ) {
                        Box(modifier = Modifier
                            .background(color = Color.LightGray)
                            .fillMaxWidth()
                        ) {
                            Text(
                                text = "Name",
                                fontSize = R.typography.bodySmall.fontSize,
                                color = R.colorScheme.primary,
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .offset(y = (-0.5).dp) //Makes text look more centered, I think?
                                    .wrapContentSize(unbounded = true) //Renders bottom of text
                            )
                        }
                    }
                }
            }
        }
    }

@Preview(showBackground = true)
@Composable
fun RankingPreview() {
    //MyScreen()
    //RankingScreen()
}