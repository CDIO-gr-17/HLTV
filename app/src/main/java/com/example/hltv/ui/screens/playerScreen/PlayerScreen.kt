package com.example.hltv.ui.screens.playerScreen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.hltv.R
import com.example.hltv.data.convertTimestampToDateURL
import com.example.hltv.data.getAvgAgeFromTimestamp
import com.example.hltv.data.getFlagFromCountryCode
import com.example.hltv.ui.common.CommonCard


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun PlayerScreen(
    playerIDFullString: String? = "Name"
) {

    val viewModel: PlayerScreenViewModel = viewModel()
    LaunchedEffect(Unit) {
        viewModel.loadData(playerIDFullString)
    }
    Log.i("playerID", "Transferred player \"ID\": $playerIDFullString")
    val player = viewModel.player.value
    val teamImage = viewModel.teamImage.value
    val playerAge = if (player?.dateOfBirthTimestamp != null)
        getAvgAgeFromTimestamp(mutableListOf(player.dateOfBirthTimestamp!!.toInt())).toInt() else 0
    val playerBorn = if (player?.dateOfBirthTimestamp != null)
        convertTimestampToDateURL(player.dateOfBirthTimestamp) else 0

    Column {
        CommonCard(
            modifier = Modifier,
            topBox = {
                Column (
                    modifier = Modifier
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    PlayerImage(image = rememberAsyncImagePainter(if(viewModel.playerImage.value!=null)viewModel.playerImage.value else R.drawable.playersilouhette))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "${player?.firstName?:""} '${player?.name?:"Nickname"}' ${player?.lastName?:""}",
                            fontSize = 24.sp,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                            fontWeight = FontWeight.Bold,
                        )
                    }
                    CommonCard (
                        modifier = Modifier
                    ) {
                        Column {
                            if(player?.firstName!=null || player?.lastName!=null) {
                                Row {
                                    Text(
                                        modifier = Modifier.weight(1f),
                                        text = "Full name:",
                                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    )
                                    Text(
                                        text = "${player.firstName?:""} ${player.lastName?:""}",
                                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    )
                                }
                                Divider(
                                    color = MaterialTheme.colorScheme.primaryContainer
                                )
                            }
                            Row {
                                Text(
                                    modifier = Modifier.weight(1f),
                                    text = "Nickname:",
                                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                )
                                Text(
                                    text = player?.name?: "Nickname",
                                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                )
                            }
                            Divider(
                                color = MaterialTheme.colorScheme.primaryContainer
                            )
                            if(player?.country?.name!=null) {
                                Row(
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        modifier = Modifier.weight(1f),
                                        text = "Country:",
                                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    )
                                    Text(
                                        text = "${player.country?.name ?: "Unknown"} ",
                                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    )
                                    Image(
                                        modifier = Modifier.size(25.dp),
                                        painter = getFlagFromCountryCode(player.country?.alpha2),
                                        contentDescription = null,
                                        alignment = Alignment.CenterEnd
                                    )
                                }
                                Divider(
                                    color = MaterialTheme.colorScheme.primaryContainer
                                )
                            }
                            if(playerAge!=0) {
                                Row {
                                    Text(
                                        modifier = Modifier.weight(1f),
                                        text = "Age:",
                                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    )
                                    Text(
                                        text = "$playerAge years old",
                                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    )
                                }
                                Divider(
                                    color = MaterialTheme.colorScheme.primaryContainer
                                )
                            }
                            if(playerBorn!=0) {
                                Row {
                                    Text(
                                        modifier = Modifier.weight(1f),
                                        text = "Born:",
                                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    )
                                    Text(
                                        text = "$playerBorn",
                                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    )
                                }
                                Divider(
                                    color = MaterialTheme.colorScheme.primaryContainer
                                )
                            }
                            if(player?.team?.name!=null && player.team?.name!="No team") {
                                Row(
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        modifier = Modifier.weight(1f),
                                        text = "Team:",
                                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    )
                                    Text(
                                        text = "${player.team?.name} ",
                                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    )
                                    Image(
                                        modifier = Modifier.size(25.dp),
                                        painter = rememberAsyncImagePainter(teamImage),
                                        contentDescription = null,
                                        alignment = Alignment.CenterEnd
                                    )
                                }
                                Divider(
                                    color = MaterialTheme.colorScheme.primaryContainer
                                )
                            }
                            if(player?.team?.country?.name!=null) {
                                Row(
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        modifier = Modifier.weight(1f),
                                        text = "Team country:",
                                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    )
                                    Text(
                                        text = "${player.team?.country?.name} ",
                                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    )
                                    Image(
                                        modifier = Modifier.size(25.dp),
                                        painter = getFlagFromCountryCode(player.team?.country?.alpha2),
                                        contentDescription = null,
                                        alignment = Alignment.CenterEnd
                                    )
                                }
                            }
                        }
                    }

                }
            }
        )
    }
}
@Composable
fun PlayerImage(
    image: Painter? = null,
) {
    Image(
        painter = image ?: painterResource(id = R.drawable.person_24px),
        contentDescription = null,
        modifier = Modifier
            .size(250.dp)
    )
}