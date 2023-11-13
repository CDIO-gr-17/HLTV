package com.example.hltv.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
//Eksempelkode for commonComposables
fun CommonComposable() {
    LazyColumn{
        item {
            //Eksempel med brug af headText og subText uden topBox
           CommonCard(
               modifier = Modifier,
               R = MaterialTheme,
               cardWidth = Modifier.fillMaxWidth(),
               headText = "Blast Premier world final 2023", //Valgfri - Erstatning for topBox
               subText = "Oct. 13 - Nov. 13", //Valgfri - Erstatning for topBox
               bottomBox = {
                   Box {
                       Text(
                           text = "Eventinformation",
                           fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                           modifier = Modifier
                               .align(Alignment.CenterStart)
                       )
                   }
               }
           )
            //Eksempel med brug af topBox og bottomBox
            CommonCard(
                modifier = Modifier,
                R = MaterialTheme,
                cardWidth = Modifier.fillMaxWidth(),
                topBox = { //Valgfri - Erstatning for headText / subText
                    Box {
                        Text(
                            text = "topBox information",
                            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                        )
                    }
                },
                bottomBox = {
                    Box {
                        Text(
                            text = "bottomBox information",
                            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                        )
                    }
                }
            )
            //Eksempel med brug af topBox uden bottomBox
            CommonCard(
                modifier = Modifier,
                R = MaterialTheme,
                cardWidth = Modifier.fillMaxWidth(),
                topBox = { //Valgfri - Erstatning for headText / subText
                    Box {
                        Text(
                            text = "topBox information",
                            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun CommonCard(
    modifier: Modifier,
    R: MaterialTheme,
    cardWidth: Modifier,
    headText: String ?= null,
    subText: String ?= null,
    topBox: @Composable (BoxScope.() -> Unit?)? = null,
    bottomBox: @Composable (BoxScope.() -> Unit?)? = null
) =
    Card (
        modifier = modifier
            .then(cardWidth)
            .padding(8.dp)
            .height(IntrinsicSize.Max)
    ) {
        Box(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.primaryContainer)
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            if (topBox != null) {
                Box() {
                    topBox()
                }
            } else
                Row (
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ){

                    Column (
                        modifier = Modifier.weight(1f)
                    ){
                        headText?.let {
                            Text(
                                text = it,
                                fontSize = R.typography.bodyLarge.fontSize,
                                color = R.colorScheme.onPrimary,
                            )
                        }
                        subText?.let {
                            Text(
                                text = it,
                                fontSize = R.typography.bodyMedium.fontSize,
                                color = R.colorScheme.onPrimary,
                            )
                        }
                    }
                    Image(
                        painter = painterResource(id = com.example.hltv.R.drawable.astralis_logo),
                        contentDescription = null,
                        alignment = Alignment.CenterEnd,
                        modifier = Modifier
                            .size(40.dp)
                    )
                }
        }
        bottomBox?.let { bottomBox ->
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .height(IntrinsicSize.Max)
                    .background(color = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                bottomBox()
            }
        }
    }


@Preview(showBackground = true)
@Composable
fun CommonComposablePreview() {
    CommonComposable()
}