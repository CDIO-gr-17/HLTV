package com.example.hltv.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
//Eksempelkode for commonComposables
fun CommonComposable() {
    LazyColumn{
        item {
            //Eksempel med brug af headText og subText uden topBox
           CommonCard(
               modifier = Modifier.fillMaxWidth(),
               headText = "Blast Premier world final 2023", //Valgfri - Erstatning for topBox
               subText = "Oct. 13 - Nov. 13", //Valgfri - Erstatning for topBox
               image = painterResource(id = com.example.hltv.R.drawable.astralis_logo)
           ) {
               Box {
                   Text(
                       text = "Eventinformation",
                       fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                       color = MaterialTheme.colorScheme.onSecondaryContainer,
                       modifier = Modifier
                           .align(Alignment.CenterStart)
                   )
               }
           }
            //Eksempel med brug af topBox og bottomBox
            CommonCard(
                modifier = Modifier.fillMaxWidth(),
                topBox = { //Valgfri - Erstatning for headText / subText
                    Box {
                        Text(
                            text = "topBox information",
                            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                        )
                    }
                }
            ) {
                Box {
                    Text(
                        text = "bottomBox information",
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                    )
                }
            }
            //Eksempel med brug af topBox uden bottomBox
            CommonCard(
                modifier = Modifier.fillMaxWidth(),
                topBox = { //Valgfri - Erstatning for headText / subText
                    Box {
                        Text(
                            text = "topBox information",
                            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                        )
                    }
                }
            )
            CommonCard (
                modifier = Modifier.fillMaxWidth()
            ) {
                Box {
                    Text(
                        text = "bottomBox information",
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                    )
                }
            }
        }
    }
}

@Composable
fun CommonCard(
    modifier: Modifier,
    headText: String ?= null,
    subText: String ?= null,
    image: Painter?= null,
    imageOnClick: (() -> Unit?)? = null,
    customOuterPadding: Dp ?= null,
    customInnerPadding: Dp ?= null,
    topBox: @Composable (BoxScope.() -> Unit?)? = null,
    bottomBox: @Composable (BoxScope.() -> Unit?)? = null,
) =
    Card (
        modifier = modifier
            .padding(customOuterPadding ?: 8.dp)
            //.height(IntrinsicSize.Max) Removed for expanded bottom. May break smth?
    ) {
        if (topBox != null || headText != null || subText != null || image != null)
            Box(
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.primaryContainer)
                    .fillMaxWidth()
                    .padding(customInnerPadding ?: 8.dp)
            ) {
                if(topBox != null)
                    topBox()
                else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            headText?.let {
                                Text(
                                    text = it,
                                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                )
                            }
                            subText?.let {
                                Text(
                                    text = it,
                                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                )
                            }
                        }
                        image?.let {
                            Image(
                                painter = image,
                                contentDescription = null,
                                alignment = Alignment.CenterEnd,
                                modifier = Modifier
                                    .size(40.dp)
                                    .clickable {
                                        if(imageOnClick!=null)
                                            imageOnClick()
                                    }


                            )
                        }
                    }
                }
        }
        bottomBox?.let { bottomBox ->
            Box(
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.secondaryContainer)
                    .fillMaxWidth()
                    .padding(customInnerPadding ?: 8.dp)
            ) {
                bottomBox()
            }
        }
    }







/*@Composable
fun FavoriteButton(isFavorite: Boolean) {
    Icon(
        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Outlined.Favorite,
      contentDescription = if (isFavorite) ("Favorited") else ("Not favorited"),
        tint = if (isFavorite) Color.Red else Color.Gray
    )
}

*/
@Preview
@Composable
fun CommonComposablePreview() {
    CommonComposable()
}