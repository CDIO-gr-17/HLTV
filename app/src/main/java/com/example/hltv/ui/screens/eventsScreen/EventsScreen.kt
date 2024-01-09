package com.example.hltv.ui.screens.eventsScreen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.hltv.R
import com.example.hltv.data.convertTimestampToDateDisplay
import com.example.hltv.ui.common.CommonCard
import java.text.NumberFormat
import java.util.Locale


@Composable
fun EventsScreen() {
    val viewModel: EventsScreenViewModel = viewModel()
    LaunchedEffect(Unit) {
        viewModel.loadData()
    }
    val tournaments = viewModel.tournaments
    val tournamentSeasons = viewModel.tournamentSeasons
    val uniqueTournaments = viewModel.uniqueTournaments

    Log.i("tournaments", "${tournaments.size}")
    Log.i("tournamentSeasons", "${tournamentSeasons.size}")
    if (tournamentSeasons.size != 0) {
        LazyColumn {
            itemsIndexed(tournaments) { index, item ->
                SingleEventCard(
                    eventTitle = "${item.name.toString()} ${tournamentSeasons[index][0].name}",
                    eventDate = if (item.startDateTimestamp != null && item.endDateTimestamp != null) {
                        "${convertTimestampToDateDisplay(item.startDateTimestamp)} - ${convertTimestampToDateDisplay(item.endDateTimestamp)}"
                    } else {
                        null
                    },
                    eventLogo = rememberAsyncImagePainter(viewModel.tournamentIcons[index]),
                    tier = uniqueTournaments[index].uniqueTournamentInfo.tier?.uppercase(),
                    prizePool = uniqueTournaments[index].uniqueTournamentInfo.totalPrizeMoney,
                    competitors = uniqueTournaments[index].uniqueTournamentInfo.numberOfCompetitors,
                    prizePoolCurrency = uniqueTournaments[index].uniqueTournamentInfo.totalPrizeMoneyCurrency
                )
            }
        }
    }
}



@Composable
fun SingleEventCard(
    eventTitle : String = "Unknown title",
    eventDate : String ?= null,
    eventLogo : Painter = painterResource(id = R.drawable.astralis_logo), //TODO: Change
    tier : String ?= null,
    prizePool : Int ?= null,
    competitors : Int ?= null,
    prizePoolCurrency : String ?= null,
) {
    CommonCard(
        modifier = Modifier,
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

