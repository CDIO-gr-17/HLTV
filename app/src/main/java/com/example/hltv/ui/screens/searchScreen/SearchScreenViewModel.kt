package com.example.hltv.ui.screens.searchScreen

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


    fun search(){
        viewModelScope.launch(Dispatchers.IO) {
            _isSearching.value = true
            val search = searchInAPIFromString(_searchQuery.value)
            _searchResults.value = search.results
            _isSearching.value = false
        }
    }
    fun setQuery(query: String){
        _searchQuery.value = query
    }

}