package com.example.hltv.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CommonComposable() {
    LazyColumn{
        item {
           TeamCard(
               modifier = Modifier,
               R = MaterialTheme,
               cardWidth = Modifier.fillMaxWidth(),
               //headText = "Blast Premier world final 2023",
               //subText = "Oct. 13 - Nov. 13",
               topBox = {
                   Box {
                       Text(
                           text = "Test",
                           fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                           modifier = Modifier
                               .align(Alignment.CenterStart)
                       )
                   }
               },
               bottomBox = {
                   Box {
                       Text(
                           text = "Test",
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
fun TeamCard(
    modifier: Modifier,
    R: MaterialTheme,
    cardWidth: Modifier,
    headText: String ?= null,
    subText: String ?= null,
    topBox: @Composable() (BoxScope.() -> Unit?)? = null,
    bottomBox: @Composable() (BoxScope.() -> Unit?)? = null
) =
    Card (
        modifier = modifier
            .then(cardWidth)
            .padding(8.dp)
            .height(IntrinsicSize.Max)
    ) {
        Box(
            modifier = Modifier
                .background(color = R.colorScheme.primary)
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
                .padding(8.dp)
        ) {if(topBox != null){
            Box() {
                topBox()
            }
        }else
            Column {
                headText?.let {
                    Text(
                        text = it,
                        fontSize = R.typography.bodyLarge.fontSize,
                        color = R.colorScheme.onSecondary,
                    )
                }
                subText?.let {
                    Text(
                        text = it,
                        fontSize = R.typography.bodyMedium.fontSize,
                        color = R.colorScheme.onSecondary,
                    )
                }
            }
        }
        bottomBox?.let { bottomBox ->
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .height(IntrinsicSize.Max)
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