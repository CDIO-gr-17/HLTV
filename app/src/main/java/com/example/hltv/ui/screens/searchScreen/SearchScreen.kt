package com.example.hltv.ui.screens.searchScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hltv.data.capitalizeFirstLetter
import com.example.hltv.data.getFlagFromCountryCode
import com.example.hltv.data.remote.Results
import com.example.hltv.ui.common.CommonCard
import kotlinx.coroutines.delay


@Composable
fun SearchScreen(
    onClickSinglePlayer: (playerID: String?) -> Unit,
    onClickSingleTeam: (teamID: String?) -> Unit,
    onClickSingleTournament: (tournamentID: String?) -> Unit
) {
    val viewModel: SearchScreenViewModel = viewModel()

    val searchText by viewModel.searchQuery.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()
    val searchResults by viewModel.searchResult.collectAsState()


    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        viewModel.setQuery(searchField())
        androidx.compose.animation.AnimatedVisibility(visible = isSearching) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.secondaryContainer)
            }
        }
        LaunchedEffect(searchText) {
            delay(300) // Adjust the delay as needed
            viewModel.search()
        }

        ShowSearchResult(
            searchResults = searchResults,
            onClickSinglePlayer = onClickSinglePlayer,
            onClickSingleTeam = onClickSingleTeam,
            onClickSingleTournament = onClickSingleTournament
        )
    }
}

@Composable
fun ShowSearchResult(
    searchResults: List<Results>,
    onClickSinglePlayer: (playerID: String?) -> Unit,
    onClickSingleTeam: (teamID: String?) -> Unit,
    onClickSingleTournament: (tournamentID: String?) -> Unit
) {
    if (searchResults.isEmpty()) {
        return
    }
    LazyColumn {
        items(searchResults.size) {
            val id = searchResults[it].entity?.id.toString()
            val type = if (searchResults[it].type == null) {
                "Unknown"
            } else if (searchResults[it].type!!.isEmpty()) {
                "Unknown"
            } else {
                capitalizeFirstLetter(searchResults[it].type!!)
            }
            val name = searchResults[it].entity?.name.toString()
            val countryCode = searchResults[it].entity?.country?.alpha2.toString()
            val flag = getFlagFromCountryCode(countryCode = countryCode)

            CommonCard(
                modifier = Modifier
                    .clickable {
                        when (searchResults[it].type) {
                            "player" -> onClickSinglePlayer(id)
                            "team" -> onClickSingleTeam(id)
                            "tournament" -> onClickSingleTournament(id)
                        }
                    },
                headText = name,
                subText = type,
                image = flag,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun searchField(): String {
    var text by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(
            TextFieldValue(
                ""
            )
        )
    }
    CommonCard(
        modifier = Modifier.fillMaxWidth(),
        topBox = {
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = text,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "SearchIcon",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                },
                onValueChange = { text = it },
                placeholder = {
                    Text(
                        text = "Search teams, players & events",
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                },
                maxLines = 1,
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(

                )
                //shape = TextFieldDefaults.outlinedShape,
            )
        }

    )
    Divider(
        modifier = Modifier
            .padding(top = 4.dp, bottom = 4.dp, start = 12.dp, end = 12.dp),
        thickness = 4.dp,
    )

    return text.text
}


@Preview
@Composable
fun SearchScreenPreview() {
    searchField()
}