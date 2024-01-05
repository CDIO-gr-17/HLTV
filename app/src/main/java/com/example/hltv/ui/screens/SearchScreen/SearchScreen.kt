package com.example.hltv.ui.screens.SearchScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun SearchScreen(
    onClickSinglePlayer: (playerID: String?) -> Unit,
    onClickSingleTeam: (teamID: String?) -> Unit,
    onClickSingleTournament: (tournamentID: String?) -> Unit
) {
    val viewModel = SearchScreenViewModel()

    val searchText by viewModel.searchQuery.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()
    val searchResults by viewModel.searchResult.collectAsState()


    Box(modifier = Modifier) {
        val text = SearchField()
        Spacer(modifier = Modifier.height(16.dp))
        Column {
            if (isSearching) {
                Text(text = "Searching...")
            } else {
                ShowSearchResult()
            }
        }

    }
}

@Composable
fun ShowSearchResult() {
    TODO("Not yet implemented")
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
                tint = MaterialTheme.colorScheme.onBackground
            )
        },
        onValueChange = { text = it },
        placeholder = {
            Text(
                text = "Enter your search query",
                color = MaterialTheme.colorScheme.onBackground
            )
        },
        singleLine = true,)
    return text.text
}

@Preview
@Composable
fun SearchScreenPreview() {
    SearchScreen(onClickSinglePlayer = {}, onClickSingleTeam = {}, onClickSingleTournament = {})
}