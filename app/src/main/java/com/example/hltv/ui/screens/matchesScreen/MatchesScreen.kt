package com.example.hltv.ui.screens.matchesScreen

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Bitmap
import android.os.SystemClock.sleep
import android.util.Log
import android.widget.DatePicker
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.hltv.R
import com.example.hltv.data.convertTimestampToWeekDateClock
import com.example.hltv.ui.common.LiveMatchCard
import com.example.hltv.ui.common.UpcomingMatchCard
import kotlinx.coroutines.launch
import java.util.Calendar


val date = mutableStateOf<Long>(0)

@Composable
fun MatchesScreen(
    onClickSingleTeam: (String?) -> Unit,
    onClickSingleMatch: (String?) -> Unit,
    onClickSingleEvent: (String?) -> Unit
) {
    val viewModel: MatchesScreenViewModel = viewModel()
    LaunchedEffect(date) {
        viewModel.loadData(date.value)
    }


    val liveMatchesValues = viewModel.liveMatchesValues
    val upcomingsMatchesValues = viewModel.upcomingMatchesValues
    val tournamentValues = viewModel.tournamentValues
    val loadingState by viewModel.loadingState.collectAsState()

    /*
        val allPlayerImages = viewModel.allPlayerImages.collectAsState()
        val playerbmap = viewModel.playerImage.collectAsState()*/

    LazyColumn {
        items(liveMatchesValues) { item ->
            LiveMatchCard(
                modifier = Modifier.clickable { onClickSingleMatch(item.id.toString()) },
                teamOneName = item.homeTeam.name.toString(),
                teamOneIcon = rememberAsyncImagePainter(
                    viewModel.homeTeamIcons[liveMatchesValues.indexOf(
                        item
                    )]
                ),
                teamOneScore = item.homeScore!!.display!!.toInt(),
                teamOneOnClick = { onClickSingleTeam(item.homeTeam.id.toString()) },
                teamTwoName = item.awayTeam.name.toString(),
                teamTwoIcon = rememberAsyncImagePainter(
                    viewModel.awayTeamIcons[liveMatchesValues.indexOf(
                        item
                    )]
                ),
                teamTwoScore = item.awayScore!!.current!!.toInt(),
            ) { onClickSingleTeam(item.awayTeam.id.toString()) }
        }
        items(upcomingsMatchesValues) { item ->
            UpcomingMatchCard(
                modifier = Modifier.clickable { onClickSingleMatch(item.id.toString()) },
                teamOneName = item.homeTeam.name.toString(),
                teamOneIcon = rememberAsyncImagePainter(
                    viewModel.homeTeamIcons[liveMatchesValues.size + upcomingsMatchesValues.indexOf(
                        item
                    )]
                ),
                teamOneOnClick = { onClickSingleTeam(item.homeTeam.id.toString()) },
                teamTwoName = item.awayTeam.name.toString(),
                teamTwoIcon = rememberAsyncImagePainter(
                    viewModel.awayTeamIcons[liveMatchesValues.size + upcomingsMatchesValues.indexOf(
                        item
                    )]
                ),
                matchDate = convertTimestampToWeekDateClock(item.startTimestamp),
                teamTwoOnClick = { onClickSingleTeam(item.awayTeam.id.toString()) },
                tournamentIcon = rememberAsyncImagePainter(
                    viewModel.tournamentIcons[tournamentValues.indexOf(
                        item
                    )]
                ),
                tournamentOnClick = { onClickSingleEvent(item.tournament.uniqueTournament?.id.toString() + "/" + item.season.id) }
            )
            //Log.i("tournamentLogo3","${viewModel.tournamentIcons.size}")
        }
        //Log.i("loadingState", "$loadingState")
        item {
            AnimatedVisibility(visible = !loadingState) {  //maybe make this a loading bar instead?
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    loadMatchesButton {
                        viewModel.viewModelScope.launch {
                            viewModel.loadUpcomingMatches()
                        }
                    }
                }
            }
            AnimatedVisibility(visible = loadingState) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.secondaryContainer)
                }
            }
        }
    }
}

@Composable
fun loadMatchesButton(function: () -> Unit) {
    Button(
        //modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer),
        onClick = function,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Row {
            Text(
                text = "Load more matches",
                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )

        }
    }
}


@Composable
fun teamCard(
    modifier: Modifier,
    materialTheme: MaterialTheme,
    text1: String = " ",
    text2: String,
    singleImgState: State<img>,
    teamPlayerImages: TeamPlayerImages?
) =

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(131.dp)
    ) {
        Box(
            modifier = Modifier
                .background(color = Color.DarkGray)
                .fillMaxWidth()
                .height(42.dp)
        ) {
            Text(
                text = text1,
                fontSize = materialTheme.typography.bodyLarge.fontSize,
                color = materialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 10.dp)
            )

        }
        Row(modifier = Modifier.padding(start = 10.dp)) {

            for (i in 1..5) {
                var bitmap: Bitmap? = null

                if (teamPlayerImages != null) {
                    if (teamPlayerImages.teamImages?.size == 0) {
                        Log.w(
                            "RankingScreen",
                            "TeamImages is of size 0. Sleeping 100ms and hoping for the best"
                        )
                        sleep(100)
                    }
                    if (teamPlayerImages.teamImages?.size != 0) {
                        bitmap = teamPlayerImages.teamImages?.get(i - 1)
                    }

                }

                val painter: AsyncImagePainter = if (bitmap == null) {
                    rememberAsyncImagePainter(R.drawable.playersilouhette)
                } else {
                    rememberAsyncImagePainter(bitmap)
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                ) {
                    Image(

                        painter = painter,
                        contentDescription = null, //TODO
                        alignment = Alignment.TopStart,
                        modifier = Modifier
                            .size(69.dp)
                            .offset(y = 10.dp)
                    )
                    Card(
                        modifier = modifier
                            .fillMaxWidth()
                            .height(12.dp)
                            .padding(end = 10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .background(color = Color.LightGray)
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = "Name",
                                fontSize = materialTheme.typography.bodySmall.fontSize,
                                color = materialTheme.colorScheme.primary,
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


@Composable
fun DatePicker(context: Context) {
    val calendar = Calendar.getInstance()
    date.value = calendar.timeInMillis

    // Initialize with current timestamp

    val datePickerDialog = DatePickerDialog(
        context, { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            calendar.set(year, month, dayOfMonth)
            date.value = calendar.timeInMillis // Update the timestamp
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.End
    ) {
        Button(onClick = {
            datePickerDialog.show()
        }, colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)) {
            Image(
                painter = painterResource(id = R.drawable.calendar_month_24px),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            )
        }
    }
}

/*
@Composable
fun DatePickerPreview() {
    // Laver fake context så den kan ses i preview
    DatePicker(context = LocalContext.current)
}
*/
@Preview(showBackground = true)
@Composable
fun MatchesScreenPreview() {
    //MatchesScreen(onClickSingleTeam = { /*TODO*/ })
}