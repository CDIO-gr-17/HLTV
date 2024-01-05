package com.example.hltv.ui.screens.SearchScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hltv.data.remote.Results
import com.example.hltv.data.remote.searchInAPIFromString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchScreenViewModel : ViewModel(){
    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _searchResults = MutableStateFlow(emptyList<Results>())
    val searchResult : MutableStateFlow<List<Results>> = _searchResults


    fun search(query: String){
        viewModelScope.launch(Dispatchers.IO) {
            _isSearching.value = true
            _searchQuery.value = query
            _searchResults.value = searchInAPIFromString(query).results


            _isSearching.value = false
        }
    }
}