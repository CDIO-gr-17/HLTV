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
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hltv.ui.common.CommonCard


@Composable
fun HomeScreen () {
    val R = MaterialTheme

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(color = R.colorScheme.background)
    ) {
        CommonCard(
            modifier = Modifier, R = MaterialTheme,
            cardWidth = Modifier.fillMaxWidth(),
            headText = "Tester lige overskriften",
            subText = "Hele tiden tester jeg"
            ) {
            Text(text ="Bottombox")
            
        }


        Card (
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .height(200.dp)


        ){
            Box(modifier = Modifier
                .background(color = R.colorScheme.primaryContainer)
                .fillMaxWidth()
                .height(50.dp)
            ) {
                Row {
                    Text(
                        text = "Your match is live!",
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxHeight()
                            .weight(1f),
                        fontSize = R.typography.bodyLarge.fontSize,
                        color = R.colorScheme.onPrimaryContainer,
                        )

                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Live Icon",
                        modifier = Modifier
                            .padding(12.dp)
                        )

                }
            }
            Row (modifier = Modifier.background(color = R.colorScheme.secondaryContainer)) {
                Column (horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth(0.5f)){
                    Text(text = "Astralis",
                        modifier = Modifier
                            .padding(8.dp),
                        fontSize = 14.sp,
                        color = R.colorScheme.onSecondaryContainer,
                        textAlign = TextAlign.Center,
                        )
                    Icon(imageVector = Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(12.dp)
                        )
                    Text(text = "10",
                        fontSize = 40.sp)
                }
                Divider(modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp)
                    .padding(vertical = 4.dp),
                    color = R.colorScheme.onBackground
                )
                Column (horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()){
                    Text(text = "Astralis",
                        modifier = Modifier
                            .padding(8.dp),
                        fontSize = 14.sp,
                        color = R.colorScheme.onSecondaryContainer,
                        textAlign = TextAlign.Center,
                    )
                    Icon(imageVector = Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(12.dp)
                    )
                    Text(text = "10",
                        fontSize = 40.sp)
                }
            }
        }
        Divider(modifier = Modifier.padding(horizontal = 8.dp), color = R.colorScheme.onBackground)
        Card (

            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .height(150.dp)


        ){
            Box(modifier = Modifier
                .background(color = R.colorScheme.primaryContainer)
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
                                .weight(1f)
                            ,
                            fontSize = R.typography.bodyLarge.fontSize,
                            color = R.colorScheme.onPrimaryContainer,

                        )
                        Text(
                            text = "Dec 13-17, 2023",
                            modifier = Modifier
                                .padding(4.dp)
                                .padding(start = 4.dp)
                                .fillMaxHeight()
                                .weight(1f),
                            fontSize = R.typography.bodyMedium.fontSize,
                            color = R.colorScheme.onPrimaryContainer,
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
            Row (modifier = Modifier.background(color = R.colorScheme.secondaryContainer)) {
                Column(
                    modifier = Modifier.weight(0.5f)
                ) {
                    Text(
                        text = "Location",
                        color = R.colorScheme.onSecondaryContainer,
                        fontSize = R.typography.bodyMedium.fontSize,
                        modifier = Modifier
                            .padding(all = 8.dp)
                            .padding(start = 8.dp)
                    )
                    Text(
                        text = "Prize pool",
                        color = R.colorScheme.onSecondaryContainer,
                        fontSize = R.typography.bodyMedium.fontSize,
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
                            color = R.colorScheme.onSecondaryContainer,
                            fontSize = R.typography.bodyMedium.fontSize,
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
                        color = R.colorScheme.onSecondaryContainer,
                        fontSize = R.typography.bodyMedium.fontSize,
                        modifier = Modifier
                            .padding(all = 8.dp)
                            .padding(end = 8.dp)


                    )
                }
            }

        }
        Divider(modifier = Modifier.padding(horizontal = 8.dp), color = R.colorScheme.onBackground)

        Text(
            text = "News",
            color = R.colorScheme.primary,
            textAlign = TextAlign.Start
            
            )

    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}