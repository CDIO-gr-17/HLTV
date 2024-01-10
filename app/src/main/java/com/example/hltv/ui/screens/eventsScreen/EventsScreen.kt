package com.example.hltv.ui.screens.eventsScreen

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.hltv.R
import com.example.hltv.data.convertTimestampToDateDisplay
import com.example.hltv.data.remote.ThirdUniqueTournament
import com.example.hltv.ui.common.CommonCard
import java.text.NumberFormat
import java.util.Locale
import androidx.compose.foundation.layout.Arrangement as Arrangement1


@Composable
fun EventsScreen(onclickSingleEvent: (String?) -> Unit) {
    val viewModel: EventsScreenViewModel = viewModel()
    LaunchedEffect(Unit) {
        viewModel.loadData()
    }
    val tournaments = viewModel.tournaments
    val tournamentSeasons = viewModel.tournamentSeasons
    val uniqueTournaments = viewModel.uniqueTournaments

    //TODO: Optimize this for CPU performance so we dont resort on redraw
    var sortedTournaments = mutableListOf<ThirdUniqueTournament>()
    val map = mutableMapOf<Int, Int>()

    val loadingState by viewModel.loadingState.collectAsState()

    //Running this inside a LaunchedEffect doesn't seem to work
    sortedTournaments.clear()
    sortedTournaments.addAll(viewModel.tournaments.sortedByDescending { it.startDateTimestamp })
    sortedTournaments.forEachIndexed{ index, tournament ->
        map.put(key = index, value = tournaments.indexOf(tournament))
    }

    LaunchedEffect(viewModel.loading) {
        Log.i("EventsScreen", "Loading changed")
    }

    Column{
        if (tournamentSeasons.size != 0) { //Crashes if this isnt there, however not the best way to implement
            LazyColumn {
                itemsIndexed(sortedTournaments) { index, item ->
                    SingleEventCard(
                        eventTitle = "${item.name.toString()} ${tournamentSeasons[map.get(index)!!][0].name}", //I think 0 is always the most recent season? Not sure tho
                        eventDate = if (item.startDateTimestamp != null && item.endDateTimestamp != null) {
                            "${convertTimestampToDateDisplay(item.startDateTimestamp)} - ${convertTimestampToDateDisplay(item.endDateTimestamp)}"
                        } else {
                            null
                        },
                        eventLogo = rememberAsyncImagePainter(viewModel.tournamentIcons[map.get(index)!!]),
                        tier = uniqueTournaments[map.get(index)!!].uniqueTournamentInfo.tier?.uppercase(),
                        prizePool = uniqueTournaments[map.get(index)!!].uniqueTournamentInfo.totalPrizeMoney,
                        competitors = uniqueTournaments[map.get(index)!!].uniqueTournamentInfo.numberOfCompetitors,
                        prizePoolCurrency = uniqueTournaments[map.get(index)!!].uniqueTournamentInfo.totalPrizeMoneyCurrency,
                        modifier = Modifier
                            .clickable { onclickSingleEvent(item.id.toString()) }
                    )
                }
                if (loadingState){
                    item{
                        Column {
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement1.Center
                            ) {
                                CircularProgressIndicator(color = MaterialTheme.colorScheme.secondaryContainer)
                            }
                        }
                    }
                }
            }
        } else if (loadingState){ //Only having this (and not the else) fails to render the loading icon as the lazycolumn gets longer. Rep of code kinda hurts but it works
            Column {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement1.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.secondaryContainer)
                }
            }
        }
    }
}



@Composable
fun SingleEventCard(
    eventTitle: String = "Unknown title",
    eventDate: String? = null,
    eventLogo: Painter = painterResource(id = R.drawable.astralis_logo), //TODO: Change
    tier: String? = null,
    prizePool: Int? = null,
    competitors: Int? = null,
    prizePoolCurrency: String? = null,
    modifier: Modifier,

    ) {
    CommonCard(
        modifier = modifier,
        headText = eventTitle,
        subText = eventDate,
        image = eventLogo,
        bottomBox = {
            Row {
                Column(
                    modifier = Modifier
                        .weight(0.6f)
                ) {
                    tier?.let {
                        Text(
                            text = "Tier",
                            modifier = Modifier
                                .padding(all = 8.dp)
                        )
                    }
                    prizePool?.let {
                        Text(
                            text = "PrizePool",
                            modifier = Modifier
                                .padding(all = 8.dp)
                        )
                    }
                    competitors?.let {
                        Text(
                            text = "Number of competitors",
                            modifier = Modifier
                                .padding(all = 8.dp)
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .weight(0.4f),
                    horizontalAlignment = Alignment.End
                ) {
                    tier?.let {
                        Text(
                            text = tier,
                            textAlign = TextAlign.End,
                            modifier = Modifier
                                .padding(8.dp)
                        )
                    }
                    prizePool?.let {
                        Text(
                            text = NumberFormat.getNumberInstance(Locale.getDefault()).format(prizePool) +
                                    " ${prizePoolCurrency}",
                            textAlign = TextAlign.End,
                            modifier = Modifier
                                .padding(8.dp)
                        )
                    }
                    competitors?.let {
                        Text(
                            text = competitors.toString(),
                            textAlign = TextAlign.End,
                            modifier = Modifier
                                .padding(8.dp)
                        )
                    }
                }
            }
        }
    )
}

