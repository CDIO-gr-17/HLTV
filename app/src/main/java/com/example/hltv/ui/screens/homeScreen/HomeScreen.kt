@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.hltv.ui.screens.homeScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.hltv.data.convertTimestampToDateClock
import com.example.hltv.ui.common.LiveMatchCard
import com.example.hltv.ui.common.UpcomingMatchCard
import com.example.hltv.ui.screens.matchesScreen.MatchesScreenViewModel

val M = MaterialTheme

@Composable
fun HomeScreen() {

    val viewModel : HomeScreenViewModel = viewModel()
    viewModel.loadData()


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .testTag("HomeScreen")
    ) {

         if (viewModel.liveMatchValue.value != null){
            LiveMatchCard(
                title = "Highlighted match",
                modifier = Modifier,
                teamOneName = viewModel.liveMatchValue.value!!.homeTeam.name?:"Unknown",
                teamOneIcon = rememberAsyncImagePainter(model = viewModel.homeTeamIcon.value),
                teamOneScore = viewModel.liveMatchValue.value!!.homeScore?.current ?: 0,
                teamOneOnClick = {},
                teamTwoName = viewModel.liveMatchValue.value!!.awayTeam.name?:"Unknown",
                teamTwoIcon = rememberAsyncImagePainter(model = viewModel.awayTeamIcon.value),
                teamTwoScore = viewModel.liveMatchValue.value!!.awayScore?.current ?: 0,
                teamTwoOnClick = {},
            )
        } else if (viewModel.upcomingMatchValue.value != null){
            UpcomingMatchCard(
                teamOneName = viewModel.upcomingMatchValue.value!!.homeTeam.name?: "Unknown",
                teamOneIcon = rememberAsyncImagePainter(model = viewModel.homeTeamIcon.value),
                teamTwoName = viewModel.upcomingMatchValue.value!!.awayTeam.name?: "Unknown",
                teamTwoIcon = rememberAsyncImagePainter(model = viewModel.awayTeamIcon.value),
                matchDate = convertTimestampToDateClock(viewModel.upcomingMatchValue.value!!.startTimestamp)
            )

         }

        Divider(modifier = Modifier.padding(horizontal = 8.dp), color = M.colorScheme.onBackground)
        Card(

            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .height(150.dp)


        ) {
            Box(
                modifier = Modifier
                    .background(color = M.colorScheme.primaryContainer)
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
            ) {
                Row {
                    Column {
                        Text(
                            text = "Blast Premier: World final 2023",
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxHeight()
                                .weight(1f),
                            fontSize = M.typography.bodyLarge.fontSize,
                            color = M.colorScheme.onPrimaryContainer,

                            )
                        Text(
                            text = "Dec 13-17, 2023",
                            modifier = Modifier
                                .padding(4.dp)
                                .padding(start = 4.dp)
                                .fillMaxHeight()
                                .weight(1f),
                            fontSize = M.typography.bodyMedium.fontSize,
                            color = M.colorScheme.onPrimaryContainer,
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.AccountBox,
                        contentDescription = "Blast icon",
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxSize()
                    )


                }

            }
            Row(modifier = Modifier.background(color = M.colorScheme.secondaryContainer)) {
                Column(
                    modifier = Modifier.weight(0.5f)
                ) {
                    Text(
                        text = "Location",
                        color = M.colorScheme.onSecondaryContainer,
                        fontSize = M.typography.bodyMedium.fontSize,
                        modifier = Modifier
                            .padding(all = 8.dp)
                            .padding(start = 8.dp)
                    )
                    Text(
                        text = "Prize pool",
                        color = M.colorScheme.onSecondaryContainer,
                        fontSize = M.typography.bodyMedium.fontSize,
                        modifier = Modifier
                            .padding(all = 8.dp)
                            .padding(start = 8.dp)
                    )
                }
                Column(
                    modifier = Modifier.weight(0.5f),
                    horizontalAlignment = Alignment.End
                ) {
                    Row {
                        Text(
                            text = "Abu Dhabi",
                            color = M.colorScheme.onSecondaryContainer,
                            fontSize = M.typography.bodyMedium.fontSize,
                            textAlign = TextAlign.End,
                            modifier = Modifier
                                .padding(8.dp)
                        )
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Abu Dhabi flag",
                            modifier = Modifier
                                .padding(4.dp)
                                .padding(end = 8.dp)
                        )
                    }

                    Text(
                        text = "$1,000,000",
                        color = M.colorScheme.onSecondaryContainer,
                        fontSize = M.typography.bodyMedium.fontSize,
                        modifier = Modifier
                            .padding(all = 8.dp)
                            .padding(end = 8.dp)


                    )
                }
            }

        }
        Divider(modifier = Modifier.padding(horizontal = 8.dp), color = M.colorScheme.onBackground)

        Text(
            text = "News",
            color = M.colorScheme.primary,
            textAlign = TextAlign.Start

        )

    }
}


@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}