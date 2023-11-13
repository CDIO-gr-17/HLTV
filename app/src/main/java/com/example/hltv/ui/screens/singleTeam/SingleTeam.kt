package com.example.hltv.ui.screens.singleTeam

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hltv.R
import com.example.hltv.ui.common.commonCard

@Composable
fun SingleTeam(){
    recentTeams()
}

@Composable
fun recentTeams(){
    commonCard(
        modifier = Modifier,
        R = MaterialTheme,
        cardWidth = Modifier.fillMaxWidth(),
        topBox = {
            Box {
                Row (
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Row (
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Image(
                            painter = painterResource(id = R.drawable.astralis_logo),
                            contentDescription = null,
                            alignment = Alignment.CenterStart,
                            modifier = Modifier
                                .size(40.dp)
                        )
                        Text(
                            text = "Astralis",
                            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    Column (
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Text(
                            text = "16-10",
                            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Text(
                            text = "10 October",
                            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    Row (
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(
                            text = "Astralis",
                            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Image(
                            painter = painterResource(id = R.drawable.astralis_logo),
                            contentDescription = null,
                            alignment = Alignment.CenterEnd,
                            modifier = Modifier
                                .size(40.dp)
                        )
                    }
                }
            }
        }
    )
}

@Composable
@Preview
fun SingleTeamPreview(){
    SingleTeam()
}