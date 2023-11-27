package com.example.hltv.ui.screens.eventsScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.hltv.R
import com.example.hltv.ui.common.CommonCard
import java.text.DateFormat.getDateInstance
import java.util.Date



@Composable
fun EventsScreen() {
    val viewModel : EventsScreenViewModel = viewModel()
    LaunchedEffect(Unit){
        viewModel.loadData()
    }
    LazyColumn {
        items(viewModel.tournaments){ item ->
            SingleEventCard(
                eventTitle = item.name.toString(),
                eventDate = convertTimestampToDate(item.startDateTimestamp),
                eventLogo = painterResource(id = R.drawable.astralis_logo),
                location = item.country?.name.toString(),
                prizePool = "Unknown",
                flagIcon = painterResource(id = R.drawable.dk_flag)
            )
        }
    }
}

private fun convertTimestampToDate(timestamp: Int?): String {
    val dateFormat = getDateInstance()
    if(timestamp != null) {
        val date = Date(timestamp.toLong()*1000)// Assuming the timestamp is in seconds, multiply by 1000 for milliseconds
        return dateFormat.format(date)
    } else {
        return "Unknown date"
    }
}

@Composable
fun SingleEventCard(
    eventTitle : String = "Unknown title",
    eventDate : String = "Unknown date",
    eventLogo : Painter = painterResource(id = R.drawable.astralis_logo), //TODO: Change
    location : String = "Unknown location",
    prizePool : String = "Unknown prize pool",
    flagIcon: Painter

) {
    CommonCard(
        modifier = Modifier,
        headText = eventTitle,
        subText = eventDate.toString(),
        image = eventLogo,
        bottomBox = {
            Row {
                Column(
                    modifier = Modifier
                        .weight(0.3f)
                ) {
                    Text(
                        text = "Location",
                        modifier = Modifier
                            .padding(all = 8.dp)

                    )
                    Text(
                        text = "PrizePool",
                        modifier = Modifier
                            .padding(all = 8.dp)

                    )                }
                Column(
                    modifier = Modifier
                        .weight(0.8f),
                    horizontalAlignment = Alignment.End
                ) {
                    Row {
                        Text(
                            text = location,
                            textAlign = TextAlign.End,
                            modifier = Modifier
                                .padding(8.dp)
                        )
                        Icon(
                            imageVector = Icons.Default.Email,  //TODO: Replace this imaage
                            contentDescription = "Country flag",
                            modifier = Modifier
                                .padding(4.dp)
                                .padding(end = 8.dp)
                                .size(20.dp),
                            tint = Color.Red

                        )
                    }

                    Text(
                        text = prizePool,
                        modifier = Modifier
                            .padding(all = 8.dp)
                            .padding(end = 8.dp)
                    )
                }
            }
        }


    )
}

