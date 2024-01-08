package com.example.hltv.ui.screens.searchScreen

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hltv.data.remote.Results
import com.example.hltv.ui.common.CommonCard
import kotlinx.coroutines.delay



@Composable
fun SearchScreen(
    onClickSinglePlayer: (playerID: String?) -> Unit,
    onClickSingleTeam: (teamID: String?) -> Unit,
    onClickSingleTournament: (tournamentID: String?) -> Unit
) {
    val viewModel : SearchScreenViewModel = viewModel()

    val searchText by viewModel.searchQuery.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()
    val searchResults by viewModel.searchResult.collectAsState()


    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        viewModel.setQuery(SearchField())


        if (isSearching) {
            Text(text = "Searching...", color = MaterialTheme.colorScheme.onPrimaryContainer)
        } else {


        }
        LaunchedEffect(searchText) {
            delay(1000) // Adjust the delay as needed
            viewModel.search()
            Log.d("Launchedeffect", "Launchedeffect is used")
        }

        if (searchResults != null){ //Android Studio says this condition is always true. That is false
            ShowSearchResult(searchResults = searchResults)
        }


    }


}

@Composable
fun ShowSearchResult(searchResults: List<Results>) {
    if(searchResults.isEmpty()){
        Log.d("ShowSearchResult", "SearchResult is empty")
        return
    }
Log.d("ShowSearchResult", "Seatchresult is not empty")
    LazyColumn {
        items(searchResults.size) {
            Log.d("LazyCollumn", "Is generated")
            when (searchResults[it].type) {
                "player" -> {
                    CommonCard(
                        modifier = Modifier,
                        headText = searchResults[it].entity?.name,

                        ) {

                    }
                }

                "team" -> {
                    CommonCard(
                        modifier = Modifier,
                        headText = searchResults[it].entity?.name,

                        )

                }

                "tournament" -> {
                    CommonCard(
                        modifier = Modifier,
                        headText = searchResults[it].entity?.name,

                        )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchField(): String {
    var text by remember { mutableStateOf(TextFieldValue("")) }
    OutlinedTextField(
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
                text = "Enter your search query",
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        },
        singleLine = true,
    )
    return text.text
}

@Preview
@Composable
fun SearchScreenPreview() {
    SearchScreen(onClickSinglePlayer = {}, onClickSingleTeam = {}, onClickSingleTournament = {})
}